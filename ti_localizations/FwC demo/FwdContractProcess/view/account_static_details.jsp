<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="gr" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Account Details</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="stylesheet" href="css/jquery-ui.css" />
<link rel="stylesheet" href="css/style.css" />
<link href="css/datepicker.css" rel="stylesheet"></link>
<link type="text/css" rel="stylesheet" href="css/bootstrap.css" />
<link />
<link type="text/css" rel="stylesheet" href="css/bootstrap-dropdown.css" />
<link />
<link type="text/css" rel="stylesheet" href="css/headfoot.css" />
<link />
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
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="js/date_search.js"></script>
<script type="text/javascript" src="js/WiseConnect.js"></script>

<script type="text/javascript" src="js/commonTiplus.js"></script>
<script type="text/javascript" src="js/staticdatasearch.js"></script>
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

                                                });/*
                                                                                    $("#buttonClose").click(function() {
                                                                                          history.go(-1);
                                                                                    }); */

                              });

      if (window.addEventListener) {
            $('body').modal({
                  show : 'false'
            });
            $('body').addClass('addPageLoad');
            window.addEventListener("load", doLoad, false);
      }
</script>

<!--[if IE 7]>
   <link rel="stylesheet" type="text/css" href="css/styleie7.css" />
 <![endif]-->
</head>

<body class="body_bg" onload="display_ct()">
      <%-- <%@ include file="/view/includes/TITLE.jsp" %>   --%>
      <img src="images/FTI-UBI.png" width="100%" />
      <gr:form method="post" id="myForm" name="form">
            <div class="row">
                  <div class="col-md-2">
                        <div class="side_nav" style="width: 200px;">
                              <ul class="nav nav-pills nav-stacked">
                                    <li style="text-align: center;"><a href="#" id="buttonClose"><gr:text
                                                      name="Close" /></a></li>
                              </ul>
                        </div>
                        <br />
                  </div>

                  <div class="col-md-10 content_box">
                        <div style="clear: both; height: 40px;"></div>
                        <%-- <h5 class="row fontcol">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<gr:text name="Customer List" /></h5> --%>
                        <div id="userIdMessage" style="color: orange"></div>
                        <div class="row page_content">
                              <div class="col-md-12">
                                    <div class="col-md-5">
                                          <div class="form-group">
                                                <label class="col-md-5 Control-label"
                                                      style="font-weight: normal;">Account Number</label>
                                                <div class="col-md-4 input-group input-group-md">
                                                      <gr:textfield id="acctNum" name="staticDataVo.acctNumber"
                                                            cssClass="form-control text_box">
                                                      </gr:textfield>
                                                </div>
                                          </div>
                                    </div>
                                    <div class="col-md-5">
                                          <div class="form-group">
                                                <label class="col-md-5 Control-label"
                                                      style="font-weight: normal;">Customer ID</label>
                                                <div class="col-md-4 input-group input-group-md">
                                                      <gr:textfield id="cusID" name="fwdContractVO.customerID"
                                                            cssClass="form-control text_box">
                                                      </gr:textfield>
                                                </div>
                                          </div>
                                    </div>

                                    <div class="col-md-2">
                                          <div class="form-group">

                                                <input type="button" value="Refresh" class="button"
                                                      onclick="fetchAccountStaticData()" />

                                          </div>
                                    </div>


                              </div>
                        </div>

                        <div style="clear: both; height: 20px;"></div>


                        <div class="row cont_colaps">
                              <div class="col-md-12">
                                    <div class="col-md-12">
                                          <div class="row page_content">
                                                <div class="page_collapsible" id="body-section1">
                                                      <span></span>
                                                      <h5 style="font-weight: bold; font-size: 13px; color: #527BB8;">
                                                            &nbsp;
                                                            <gr:text name="Account Details" />
                                                      </h5>
                                                </div>
                                                <div class="form-group">
                                                      <!-- <div class="table"> -->
                                                      <table border="1px" align="left">
                                                            <tbody>
                                                                  <tr>
                                                                        <th align="left" style="width: 400px; padding: 4px 5px;">Account
                                                                              Number</th>
                                                                        <th align="left" style="width: 400px; padding: 4px 5px;">Account
                                                                              Type</th>
                                                                        <th align="left" style="width: 400px; padding: 4px 5px;">Branch
                                                                        </th>
                                                                        <th align="left" style="width: 400px; padding: 4px 5px;">Customer
                                                                              ID</th>
                                                                        <th align="left" style="width: 400px; padding: 4px 5px;">Short
                                                                              Name</th>
                                                                        <th align="left" style="width: 400px; padding: 4px 5px;">Currency</th>

                                                                  </tr>
                                                                  <c:if test="${empty staticDataList}">
                                                                        <tr>
                                                                              <td colspan="6">No records found</td>
                                                                        </tr>
                                                                  </c:if>
                                                                  <c:if test="${not empty staticDataList}">

                                                                        <gr:iterator value="staticDataList" id="staticDataList">
                                                                              <tr class="accountList" ondblclick="selectAcctNumber(this)">
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="acctNumber" />
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>

                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="acctType" />
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="branchCode" />
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="customerID" />
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="shortName" />
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>
                                                                                    <td style="padding: 4px 5px;">
                                                                                          <div class="form-group" align="left">
                                                                                                <gr:property value="currency" />
                                                                                                <div class="col-md-8 input-group input-group-md"></div>
                                                                                          </div>
                                                                                    </td>

                                                                              </tr>
                                                                        </gr:iterator>
                                                                  </c:if>
                                                            </tbody>
                                                      </table>
                                                      <br /> <br />
                                                      <!-- </div> -->
                                                </div>
                                          </div>
                                    </div>
                              </div>
                        </div>



                        <div>
                              <gr:hidden id="screenType" name="fwdContractVO.screenType"></gr:hidden>
                              <%-- <gr:hidden id="customerID" name="fwdContractVO.customerID"></gr:hidden> --%>
                              <gr:hidden id="branchCode" name="fwdContractVO.branchCode"></gr:hidden>
                              <gr:hidden id="acctNumber" name="fwdContractVO.acctNumber"></gr:hidden>
                              <gr:hidden id="dealCurrency" name="fwdContractVO.dealCurrency"></gr:hidden>
                              <gr:hidden id="treasuryRefNo" name="fwdContractVO.treasuryRefNo"></gr:hidden>
                              <gr:hidden id="limitID" name="fwdContractVO.limitID"></gr:hidden>
                              <gr:hidden name="fwdContractVO.subProduct"></gr:hidden>
                              <gr:hidden name="fwdContractVO.fwdContractNo"></gr:hidden>
                              <gr:hidden name="fwdContractVO.bookingDate"></gr:hidden>
                              <gr:hidden name="fwdContractVO.toCurrency"></gr:hidden>
                              <gr:hidden name="fwdContractVO.dealValidFromDate"></gr:hidden>
                              <gr:hidden name="fwdContractVO.outstandingAmt"></gr:hidden>
                              <gr:hidden name="fwdContractVO.fwdContractAmt"></gr:hidden>
                              <gr:hidden name="fwdContractVO.toCurrencyAmt"></gr:hidden>
                              <gr:hidden name="fwdContractVO.dealValidToDate"></gr:hidden>
                              <gr:hidden name="fwdContractVO.treasuryRate"></gr:hidden>
                              <gr:hidden name="fwdContractVO.withoutLimit"></gr:hidden>
                              <gr:hidden name="fwdContractVO.availableLimit"></gr:hidden>
                              <gr:hidden name="fwdContractVO.washRate"></gr:hidden>
                              <gr:hidden name="fwdContractVO.plAmount"></gr:hidden>
                              <gr:hidden name="fwdContractVO.instructions"></gr:hidden>
                              <gr:hidden name="fwdContractVO.leiNumber"></gr:hidden>
                              <gr:hidden name="fwdContractVO.chargeAmount"></gr:hidden>
                              <gr:hidden name="fwdContractVO.gstAmount"></gr:hidden>
                              <gr:hidden name="fwdContractVO.bookingRate"></gr:hidden>
                              <gr:hidden name="fwdContractVO.margin"></gr:hidden>
                        </div>
                  </div>
            </div>
      </gr:form>
      <div id="footer">
            <%@ include file="/view/includes/FOOTER.jsp"%>
      </div>
</body>
</html>