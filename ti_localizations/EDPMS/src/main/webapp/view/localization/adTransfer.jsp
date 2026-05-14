<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="boe" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>AD Transfer</title>

<link rel="stylesheet" href="css/dashboard.css" />
<link rel="stylesheet" type="text/css" href="css/adTransfer.css"/>

<script type="text/javascript">

function windowClose()
{
    top.close();
}

function resetForm()
{
    document.getElementById("adTransferForm").reset();
}

</script>

</head>

<body>

<!-- =====================================================
     HEADER
===================================================== -->

<div class="header">

    <div class="system-date">
        System Date : 09-10-2025
    </div>

    <div class="logo-right">
        <img src="images/finastra-logo.png" alt="Union Bank"/>
    </div>

</div>

<!-- =====================================================
     LEFT PANEL
===================================================== -->

<div class="left-panel">

    <div class="close-btn">

        <boe:if test="%{#session.xEvtRefNum.length() > 0}">
            <a href="#" onclick="windowClose()">Close</a>
        </boe:if>

        <boe:else>
            <a href="home">Close</a>
        </boe:else>

    </div>

    <div class="action-section">

        <input type="button"
               value="OK"
               class="side-btn"/>

        <input type="button"
               value="Validate"
               class="side-btn"/>

        <input type="button"
               value="Reset"
               class="side-btn"
               onclick="resetForm()" />

    </div>

    <div class="left-line"></div>

</div>

<!-- =====================================================
     MAIN CONTENT
===================================================== -->

<div class="main-content">

    <div class="content-box">

        <div class="ad-panel">

            <!-- =========================================
                 TITLE
            ========================================== -->

            <div class="section-header">
                <span class="expand-icon">⊖</span>
                AD Transfer
            </div>

            <!-- =========================================
                 ERROR DESCRIPTION
            ========================================== -->

            <div class="section-header">
                <span class="expand-icon">⊖</span>
                Error Description
            </div>

            <div class="table-wrapper">

                <table class="details-table">

                    <thead>

                        <tr>

                            <th>Severity</th>
                            <th>Description</th>
                            <th>Steps</th>
                            <th>Details</th>
                            <th>Overrides</th>

                        </tr>

                    </thead>

                    <tbody>

                        <tr>
                            <td colspan="5" class="empty-row"></td>
                        </tr>

                    </tbody>

                </table>

            </div>

            <!-- =========================================
                 INPUT DETAILS
            ========================================== -->

            <div class="section-header mt-10">
                <span class="expand-icon">⊖</span>
                Input Details
            </div>

            <form id="adTransferForm"
                  action="adTransfer"
                  method="post">

                <table class="form-table">

                    <tr>

                        <td class="label-field">
                            Shipping Bill No
                        </td>

                        <td>
                            <input type="text"
                                   name="shippingBillNo"
                                   class="textbox-field"/>
                        </td>

                        <td class="label-field">
                            Shipping Bill Date
                        </td>

                        <td>
                            <input type="text"
                                   name="shippingBillDate"
                                   class="textbox-field"/>
                        </td>

                    </tr>

                    <tr>

                        <td class="label-field">
                            Port Code
                        </td>

                        <td>
                            <input type="text"
                                   name="portCode"
                                   class="textbox-field"/>
                        </td>

                        <td class="label-field">
                            Form No
                        </td>

                        <td>
                            <input type="text"
                                   name="formNo"
                                   class="textbox-field"/>
                        </td>

                    </tr>

                    <tr>

                        <td class="label-field">
                            Export Type
                        </td>

                        <td>

                            <select name="exportType"
                                    class="dropdown-field">

                                <option>Goods</option>
                                <option>Services</option>

                            </select>

                        </td>

                        <td class="label-field">
                            IE Code
                        </td>

                        <td>
                            <input type="text"
                                   name="ieCode"
                                   class="textbox-field"/>
                        </td>

                    </tr>

                    <tr>

                        <td class="label-field">
                            Existing ADCode
                        </td>

                        <td>
                            <input type="text"
                                   name="existingAdCode"
                                   class="textbox-field"/>
                        </td>

                        <td class="label-field">
                            New ADCode
                        </td>

                        <td>
                            <input type="text"
                                   name="newAdCode"
                                   class="textbox-field"/>
                        </td>

                    </tr>

                    <tr>

                        <td class="label-field">
                            Export Agency
                        </td>

                        <td>

                            <select name="exportAgency"
                                    class="dropdown-field">

                                <option>Customs</option>

                            </select>

                        </td>

                    </tr>

                </table>

            </form>

            <!-- =========================================
                 TRANSFER DETAILS
            ========================================== -->

            <div class="section-header mt-10">
                <span class="expand-icon">⊖</span>
                Transfer Details
            </div>

            <div class="table-wrapper">

                <table class="details-table">

                    <thead>

                        <tr>

                            <th></th>
                            <th>Shipping Bill No</th>
                            <th>Shipping Bill Date</th>
                            <th>Port Code</th>
                            <th>IE Code</th>
                            <th>Form No</th>
                            <th>Export Agency</th>
                            <th>Type of Export</th>
                            <th>Existing AdCode</th>
                            <th>New ADCode</th>

                        </tr>

                    </thead>

                    <tbody>

                        <tr>


                        </tr>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

</div>

</body>

</html>