<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="gr" uri="/struts-tags"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Forward Contract Enquiry View</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<link rel="stylesheet" href="css/jquery-ui.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/datepicker.css" />
<link type="text/css" rel="stylesheet" href="css/bootstrap.css" />
<link type="text/css" rel="stylesheet" href="css/bootstrap-dropdown.css" />
<link type="text/css" rel="stylesheet" href="css/headfoot.css" />
<link href="css/font-awesome.css" rel="stylesheet" />
<link rel="stylesheet" href="css/commonTiplus.css" />
<script src="js/jquery-1.9.1.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/bootstrap.js" type="text/javascript"></script>
<script src="js/bootstrap-datepicker.js" type="text/javascript"></script>
<script type="text/javascript" src="js/macxdnie81.js"></script>
<script type="text/javascript" src="js/maxcdnie82.js"></script>
<script type="text/javascript" src="js/jquery.cookie.min.js"></script>
<script type="text/javascript" src="js/jquery.collapsible.min.js"></script>
<script type="text/javascript" src="js/highlight.pack.js"></script>

<script type="text/javascript" src="js/date_search.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/commonTiplus.js"></script>
<script type="text/javascript" src="js/forwardcontract.js"></script>

<script>
      $(document)
                  .ready(
                              function() {
                                    
                                    $(
                                                'input[type="submit"],a,input[type="button"],onchange,onblur')
                                                .click(function() {
                                                      //$('input[type="submit"],a,input[type="button"]').click(function(){
                                                      $('body').modal({
                                                            show : 'false'
                                                      });
                                                      $('body').removeClass('removePageLoad');
                                                      $('body').addClass('addPageLoad');

                                                });

                              });

      if (window.addEventListener) {
            $('body').modal({
                  show : 'false'
            });
            $('body').addClass('addPageLoad');
            window.addEventListener("load", doLoad, false);
      }
</script>

</head>

<body class="body_bg" onload="display_ct()">
      <%-- <%@ include file="/view/includes/TITLE.jsp"%> --%>
      <img src="images/FTI-UBI.png" width="100%" />
      <gr:form method="post" id="myForm" name="form">
            <div class="row">

                  <div class="col-md-2">
                        <div class="side_nav" style="width: 215px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;">
                                          <!-- <a href="enquiryProcess">Close</a>  -->
                                          <a href="javascript:closeView()"><gr:text name="Close" /></a>
                                    </li>
                              </ul>
                        </div>
                        <br />
                  </div>

                  <div class="col-md-10 content_box">
                        <div class="row page_content">

                              <c:if test="${not empty msg}">
                                    <h5 style="font-weight: lighter; font-size: 13px; color: #FF0000;">
                                          <gr:property value="msg" />
                                    </h5>
                              </c:if>
                        </div>
                        <br />

                        <div id="userIdMessage" style="color: orange"></div>

                        <br />
                        
                                    <!-- Posting starts -->
                        
                        
                        <div class="row page_content">
                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px; color: #527BB8;">&nbsp;Postings</h5>
                                    </div>

                              <div class="table">
                                    <table border="1px" align="left" id="postingList">
                                          <tbody>
                                                <tr>
                                                      <!-- <th style="text-align: left; width: 200px"><label>&nbsp;&nbsp;Type</label></th> -->

                                                      <th style="text-align: left; width: 50px"><label>&nbsp;&nbsp;Dr/Cr</label></th>

                                                      <th style="text-align: left; width: 200px"><label>&nbsp;&nbsp;Account</label></th>

                                                      <th style="text-align: left; width: 200px"><label>&nbsp;&nbsp;Amount</label></th>

                                                      <th style="text-align: left; width: 150px"><label>&nbsp;&nbsp;Value
                                                                  Date</label></th>

                                                      <th style="text-align: left; width: 500px"><label>&nbsp;&nbsp;Description</label></th>
                                                </tr>
                                                            <gr:iterator value="fwdContractVO.postingList"
                                                                  id="postingList">
                                                                  <tr>
                                                                  <%--  <td>
                                                                              <div class="form-group" align="left">
                                                                                    <gr:property value="postingType" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>

                                                                        </td> --%>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <gr:property value="postingDrCrFlag" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <gr:property value="postingAcctNumber" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <gr:property value="postingAmountCcy" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <gr:property value="postingValueDate" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                        <td>
                                                                              <div class="form-group" align="left">
                                                                                    <gr:property value="postingDesc" />
                                                                                    <div class="col-md-8 input-group input-group-md"></div>
                                                                              </div>
                                                                        </td>
                                                                  </tr>
                                                            </gr:iterator>

                                                </tbody>
                                    </table>
                              </div>
                        
                        
                        
                        </div>
                        </div>
                        
                        <br /><br />
                        
                  <!--  Posting ends -->
                  
                        <div class="row cont_colaps">
                              <div class="col-md-12">
                                    <div class="page_collapsible" id="body-section1">
                                          <span></span>
                                          <h5 style="font-weight: bold; font-size: 13px; color: #527BB8;">&nbsp;Forward Contract
                                                Details</h5>
                                    </div>

                                    <div class="col-md-12">

                                          <div class="row page_content">
                                                <div class="col-md-6">
                                                      <div class="form-group">
                                                            <label class="col-md-5 Control-label"
                                                                  style="font-weight: normal;">Sub Product</label>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="fwdContractNo"
                                                                        name="fwdContractVO.subProduct" readonly="true"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                </div>

                                                <div class="col-md-6">
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Forward Contract
                                                                        Number &nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="fwdContractNo"
                                                                        name="fwdContractVO.fwdContractNo" readonly="true"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                </div>
                                          </div>

                                          <div class="row page_content">

                                                <div>
                                                      <p
                                                            style="font-weight: bold; color: #527BB8; text-decoration: underline; margin-left: 20px;">
                                                            Customer Details</p>
                                                </div>
                                                <div class="col-md-6">
                                                      <div class="form-group required">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Customer ID&nbsp;<span
                                                                        class="searchBtn"
                                                                        style="color: #579EDC; font-weight: bold; cursor: pointer;"
                                                                        onclick="customerStaticDetails()"> <img
                                                                              src="images/magnify.png" width="13%" height="13%" /></span></label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="customerID" name="fwdContractVO.customerID"
                                                                        readonly="true" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Account
                                                                        Number&nbsp;<span class="searchBtn"
                                                                        style="color: #579EDC; font-weight: bold; cursor: pointer;"
                                                                        onclick="accountStaticDetails()"> <img
                                                                              src="images/magnify.png" width="13%" height="13%" /></span>
                                                                  </label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="acctNumber" name="fwdContractVO.acctNumber"
                                                                        readonly="true" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>

                                                </div>
                                                <div class="col-md-6">
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Branch Code<span
                                                                        class="searchBtn"
                                                                        style="color: #579EDC; font-weight: bold; cursor: pointer;"
                                                                        onclick="branchStaticDetails()"> <img
                                                                              src="images/magnify.png" width="13%" height="13%" /></span></label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="branchCode" name="fwdContractVO.branchCode"
                                                                        readonly="true" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Deal Currency<span
                                                                        class="searchBtn"
                                                                        style="color: #579EDC; font-weight: bold; cursor: pointer;"
                                                                        onclick="currencyStaticDetails()"> <img
                                                                              src="images/magnify.png" width="13%" height="13%" /></span></label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="dealCurrency"
                                                                        name="fwdContractVO.dealCurrency" readonly="true"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                </div>
                                          </div>

                                          <div class="row page_content">
                                                <div>
                                                      <p
                                                            style="font-weight: bold; color: #527BB8; text-decoration: underline; margin-left: 20px;">
                                                            Contract Details</p>
                                                </div>
                                                <div class="col-md-6">

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Treasury
                                                                        Reference Number</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="treasuryRefNo"
                                                                        name="fwdContractVO.treasuryRefNo" readonly="true"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> To Amount</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="toCurrencyAmt"
                                                                        name="fwdContractVO.toCurrencyAmt" readonly="true"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Deal Valid From
                                                                        &nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="dealValidFromDate"
                                                                        name="fwdContractVO.dealValidFromDate"
                                                                        cssClass="datepicker form-control text_box" readonly="true"
                                                                        style="text-align: left;" />
                                                            </div>
                                                      </div>
                                                      
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Booking Date
                                                                        &nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="bookingDate"
                                                                        name="fwdContractVO.bookingDate"
                                                                        cssClass="datepicker form-control text_box" readonly="true"
                                                                        style="text-align: left;" />
                                                            </div>
                                                      </div>
                                          

                                                </div>
                                                <div class="col-md-6">
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Forward Contract
                                                                        Amount</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="fwdContractAmt"
                                                                        name="fwdContractVO.fwdContractAmt" readonly="true"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Outstanding
                                                                        Amount</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="outstandingAmt"
                                                                        name="fwdContractVO.outstandingAmt" readonly="true"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>



                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Deal Valid
                                                                        To&nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="dealValidToDate"
                                                                        name="fwdContractVO.dealValidToDate" readonly="true"
                                                                        cssClass="datepicker form-control text_box"
                                                                        style="text-align: left;" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Treasury Rate</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="treasuryRate"
                                                                        name="fwdContractVO.treasuryRate" readonly="true"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>


                                                </div>

                                          </div>

                                                      <div class="row page_content">
                                                <div>
                                                      <p
                                                            style="font-weight: bold; color: #527BB8; text-decoration: underline; margin-left: 20px;">
                                                            Limit Details</p>
                                                </div>
                                                <div class="col-md-6">

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Limit ID
                                                                  </label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="limitID" name="fwdContractVO.limitID"
                                                                        readonly="true" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Available
                                                                        Limit&nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="availableLimit"
                                                                        name="fwdContractVO.availableLimit" readonly="true"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>

                                                </div>
                                                
                                                <div class="col-md-6">

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Without
                                                                        Limit&nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:checkbox id="withoutLimit"
                                                                        name="fwdContractVO.withoutLimit" disabled="disabled"/>
                                                            </div>
                                                      </div>

                                                </div>
                                                
                                          </div>

                                          <div class="row page_content">
                                                <div>
                                                      <p
                                                            style="font-weight: bold; color: #527BB8; text-decoration: underline; margin-left: 20px;">
                                                            Charge Details</p>
                                                </div>
                                                <div class="col-md-6">
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Charge Amount</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="chargeAmount"
                                                                        name="fwdContractVO.chargeAmount" readonly="true"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> GST Amount</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="gstAmount"
                                                                        name="fwdContractVO.gstAmount" readonly="true"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                </div>
                                          </div>


                                          <div class="row page_content">
                                                <div>
                                                      <p
                                                            style="font-weight: bold; color: #527BB8; text-decoration: underline; margin-left: 20px;">
                                                            Additional Details</p>
                                                </div>
                                                <div class="col-md-6">

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Wash Rate
                                                                        &nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="washRate" name="fwdContractVO.washRate"
                                                                        readonly="true" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>


                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> P&L Amount</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="plAmount"
                                                                        name="fwdContractVO.plAmount" readonly="true"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>


                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Instructions&nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textarea id="instructions"
                                                                        name="fwdContractVO.instructions" cols="18" rows="4"
                                                                        readonly="true"
                                                                        style="margin: 0px; width: 200px; height: 74px;" />
                                                            </div>
                                                      </div>

                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">Remarks&nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textarea id="remarks" name="fwdContractVO.remarks"
                                                                        cols="18" rows="4" readonly="true"
                                                                        style="margin: 0px; width: 200px; height: 74px;" />
                                                            </div>
                                                      </div>


                                                </div>
                                                <div class="col-md-6">


                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;">LEI Number
                                                                        &nbsp;&nbsp;</label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="leiNumber" name="fwdContractVO.leiNumber"
                                                                        readonly="true" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>

                                                      
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Margin </label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="margin" name="fwdContractVO.margin"
                                                                        readonly="true" cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                      <!-- added by anand singh -->
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Transaction ID </label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="transid" name="fwdContractVO.transid"
                                                                        cssClass="form-control text_box" />
                                                            </div>
                                                      </div>
                                                      
                                                      <div class="form-group">
                                                            <div class="col-md-5 Control-label">
                                                                  <label style="font-weight: normal;"> Transaction Date </label>
                                                            </div>
                                                            <div class="col-md-3 input-group input-group-md">
                                                                  <gr:textfield id="transdate"
                                                                        name="fwdContractVO.transdate" readonly="false"
                                                                        cssClass="datepicker form-control text_box"
                                                                        style="text-align: left;" />
                                                            </div>
                                                      </div>
                                                      
                                                </div>

                                          </div>
                                    </div>
                              </div>

                        </div>

                        <div>
                              <gr:hidden id="screenType" name="fwdContractVO.screenType"></gr:hidden>
                        </div>
                        <div style="clear: both; height: 20px;"></div>
                  </div>
            </div>
      </gr:form>
      <div id="footer">
            <%@ include file="/view/includes/FOOTER.jsp"%>
      </div>
</body>
</html>