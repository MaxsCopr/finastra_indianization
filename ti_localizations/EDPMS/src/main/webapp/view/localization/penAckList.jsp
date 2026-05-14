<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Payment Extension Acknowledgement</title>
<link rel="stylesheet" type="text/css"
    href="${pageContext.request.contextPath}/css/penAckList.css">
</head>
<body>

<div class="main-wrapper">

    <!-- Header Section -->
    <div class="top-header">
        <div class="logo-section">
            <img src="${pageContext.request.contextPath}/images/logo.png"
                 alt="Bank Logo"
                 class="left-logo">
        </div>



    <div class="logo-right">
        <img src="images/finastra-logo.png" alt="Union Bank"/>
    </div>
    </div>

    <!-- Side Menu -->
    <div class="content-wrapper">

        <div class="left-panel">
            <div class="system-date">
                System Date : <s:property value="systemDate"/>
            </div>

            <button class="menu-btn" ">
                <a href="acknowledgement">Close</a>
            </button>

            <button class="menu-btn" onclick="resetForm();">
                Reset
            </button>
        </div>

        <!-- Main Content -->
        <div class="main-content">
	        <div class="header-title">
            Payment Extension Acknowledgement
        </div>
            <!-- Input Details -->
            <fieldset>
                <legend>Input Details</legend>

                <table class="form-table">
                    <tr>
                        <td class="label">FEN Created From</td>
                        <td>
                            <s:textfield name="fenCreatedFrom"
                                         cssClass="input-box"/>
                        </td>

                        <td class="label">To</td>
                        <td>
                            <s:textfield name="fenCreatedTo"
                                         cssClass="input-box"/>
                        </td>

                        <td class="label">Shipping Bill No</td>
                        <td>
                            <s:textfield name="shippingBillNo"
                                         cssClass="input-box small-box"/>
                        </td>
                    </tr>

                    <tr>
                        <td class="label">Shipping Bill Date</td>
                        <td>
                            <s:textfield name="shippingBillDate"
                                         cssClass="input-box"/>
                        </td>

                        <td class="label">Port Code</td>
                        <td>
                            <s:textfield name="portCode"
                                         cssClass="input-box small-box"/>
                        </td>

                        <td>
                            <input type="button"
                                   value="Refresh"
                                   class="refresh-btn" />
                        </td>
                    </tr>

                    <tr>
                        <td class="label">Form No</td>
                        <td>
                            <s:textfield name="formNo"
                                         cssClass="input-box small-box"/>
                        </td>
                    </tr>
                </table>
            </fieldset>

            <!-- PEN Acknowledgement Details -->
            <fieldset>
                <legend>PEN Acknowledgement Details</legend>

                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Shipping Bill No</th>
                                <th>Shipping Bill Date</th>
                                <th>Port Code</th>
                                <th>Form No</th>
                                <th>Code</th>
                                <th>AD Code</th>
                                <th>Export Type</th>
                                <th>Record Number</th>
                                <th>LEO Date</th>
                                <th>Realization Certificate No</th>
                                <th>Letter No</th>
                                <th>Letter Date</th>
                                <th>Extend Realization Date</th>
                                <th>Error Code</th>
                            </tr>
                        </thead>

                        <tbody>
                            <s:if test="penAckList != null && penAckList.size > 0">
                                <s:iterator value="penAckList">
                                    <tr>
                                        <td><s:property value="shippingBillNo"/></td>
                                        <td><s:property value="shippingBillDate"/></td>
                                        <td><s:property value="portCode"/></td>
                                        <td><s:property value="formNo"/></td>
                                        <td><s:property value="code"/></td>
                                        <td><s:property value="adCode"/></td>
                                        <td><s:property value="exportType"/></td>
                                        <td><s:property value="recordNumber"/></td>
                                        <td><s:property value="leoDate"/></td>
                                        <td><s:property value="realizationCertificateNo"/></td>
                                        <td><s:property value="letterNo"/></td>
                                        <td><s:property value="letterDate"/></td>
                                        <td><s:property value="extendRealizationDate"/></td>
                                        <td><s:property value="errorCode"/></td>
                                    </tr>
                                </s:iterator>
                            </s:if>

                            <s:else>
                                <tr>
                                    <td colspan="14" class="no-records">
                                        No records found
                                    </td>
                                </tr>
                            </s:else>
                        </tbody>
                    </table>
                </div>
            </fieldset>
        </div>
    </div>
</div>

<script>
function resetForm() {
    document.forms[0].reset();
}
</script>

</body>
</html>