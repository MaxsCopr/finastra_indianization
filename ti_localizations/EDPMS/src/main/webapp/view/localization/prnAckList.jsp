<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Payment Realization Acknowledgement</title>

<link rel="stylesheet" type="text/css"
    href="${pageContext.request.contextPath}/css/prnAckList.css">
</head>

<body>

<div class="main-container">

    <!-- Header Section -->
    <div class="top-header">
        <div class="logo-section">
            <img src="${pageContext.request.contextPath}/images/logo.png"
                 alt="Logo"
                 class="left-logo">
        </div>

    <div class="logo-right">
        <img src="images/finastra-logo.png" alt="Union Bank"/>
    </div>
    </div>


    <!-- Left Menu -->
    <div class="left-panel">
        <button class="menu-btn"><a href="acknowledgement">Close</a></button>
        <button class="menu-btn">Reset</button>
    </div>


    <!-- Content Section -->
    <div class="content-panel">


        <!-- Input Details -->
        <fieldset class="section-box">
                <div class="page-title">
            Payment Realization Acknowledgement
        </div>
            <legend>Input Details</legend>

            <table class="form-table">
                <tr>
                    <td class="label">PRN Created From</td>
                    <td>
                        <input type="text" name="prnFrom" class="input-field">
                    </td>

                    <td class="label">To</td>
                    <td>
                        <input type="text" name="prnTo" class="input-field">
                    </td>

                    <td class="label">Shipping Bill No</td>
                    <td>
                        <input type="text" name="shippingBillNo"
                               class="input-field">
                    </td>
                </tr>

                <tr>
                    <td class="label">Shipping Bill Date</td>
                    <td>
                        <input type="text" name="shippingBillDate"
                               class="input-field">
                    </td>

                    <td class="label">&nbsp;</td>
                    <td>&nbsp;</td>

                    <td class="label">Port Code</td>
                    <td>
                        <input type="text" name="portCode"
                               class="input-field">
                    </td>
                </tr>

                <tr>
                    <td class="label">Payment Sequence No</td>
                    <td>
                        <input type="text" name="paymentSequenceNo"
                               class="input-field">
                    </td>

                    <td class="label">&nbsp;</td>
                    <td>&nbsp;</td>

                    <td class="label">Form No</td>
                    <td>
                        <input type="text" name="formNo"
                               class="input-field small-input">
                    </td>
                </tr>
            </table>

            <div class="button-row">
                <s:submit value="Refresh"
                          cssClass="refresh-btn"
                          action="prnAckAction"/>
            </div>

        </fieldset>


        <!-- Shipping Acknowledgement Details -->
        <fieldset class="section-box">
            <legend>PRN Shipping Acknowledgement Details</legend>

            <div class="table-wrapper">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Shipping Bill No</th>
                            <th>Shipping Bill Date</th>
                            <th>Port Code</th>
                            <th>Form No</th>
                            <th>Receiver Indicator</th>
                            <th>Export Type</th>
                            <th>LEO Date</th>
                            <th>AD Code</th>
                            <th>Payment Sequence</th>
                            <th>PRN Number</th>
                            <th>PRN Date</th>
                            <th>Remitter ADCode</th>
                            <th>Realization Date</th>
                            <th>Realization Currency</th>
                            <th>Realized Party</th>
                            <th>IFSC Code</th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr>
                            <td colspan="16" class="no-records">
                                No records found
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </fieldset>


        <!-- Invoice Acknowledgement Details -->
        <fieldset class="section-box">
            <legend>PRN Invoice Acknowledgement Details</legend>

            <div class="table-wrapper">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Invoice Serial No</th>
                            <th>Invoice No</th>
                            <th>Invoice Date</th>
                            <th>Invoice Amount</th>
                            <th>Banking Charges</th>
                            <th>FCB Amt</th>
                            <th>FCB Article</th>
                            <th>Freight Amt</th>
                            <th>Freight Article</th>
                            <th>Insurance Amt</th>
                            <th>Insurance Article</th>
                            <th>Close Bill IND</th>
                            <th>Remitter Name</th>
                            <th>Remitter Country</th>
                            <th>Invoice Currency Code</th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr>
                            <td colspan="15" class="no-records">
                                No records found
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </fieldset>

    </div>
</div>

</body>
</html>