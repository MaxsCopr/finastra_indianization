<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="boe" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>Receipt Of Document Acknowledgement</title>
<link rel="stylesheet" href="css/dashboard.css" />
<link rel="stylesheet" type="text/css"
      href="css/receiptOfDocumentAcknowledgement.css"/>

<script type="text/javascript">

function windowClose()
{
    top.close();
}

function resetForm()
{
    document.getElementById("rodForm").reset();
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
            <a href="acknowledgement">Close</a>
        </boe:else>

    </div>

    <div class="reset-section">

        <input type="button"
               value="Reset"
               class="reset-btn"
               onclick="resetForm()" />

    </div>

    <div class="left-line"></div>

</div>

<!-- =====================================================
     MAIN CONTENT
===================================================== -->

<div class="main-content">

    <div class="content-box">

        <div class="rod-panel">

            <!-- =========================================
                 PAGE TITLE
            ========================================== -->

            <div class="page-title">
                Receipt of Document Acknowledgement
            </div>
            

            <!-- =========================================
                 INPUT DETAILS
            ========================================== -->

            <div class="section-header">
                <span class="expand-icon">⊖</span>
                Input Details
            </div>

            <form id="rodForm"
                  action="receiptOfDocumentAcknowledgement"
                  method="post">

                <table class="search-form-table">

                    <tr>

                        <td class="search-label">
                            ROD Created From
                        </td>

                        <td>
                            <input type="text"
                                   name="rodCreatedFrom"
                                   class="search-textbox"/>
                        </td>

                        <td class="search-label">
                            To
                        </td>

                        <td>
                            <input type="text"
                                   name="rodCreatedTo"
                                   class="search-textbox"/>
                        </td>

                        <td class="search-label">
                            Shipping Bill No
                        </td>

                        <td>
                            <input type="text"
                                   name="shippingBillNo"
                                   value="123654798"
                                   class="search-textbox"/>
                        </td>

                    </tr>

                    <tr>

                        <td class="search-label">
                            Shipping Bill Date
                        </td>

                        <td>
                            <input type="text"
                                   name="shippingBillDate"
                                   class="search-textbox"/>
                        </td>

                        <td></td>
                        <td></td>

                        <td class="search-label">
                            Port Code
                        </td>

                        <td>
                            <input type="text"
                                   name="portCode"
                                   value="123456"
                                   class="search-textbox"/>
                        </td>

                    </tr>

                    <tr>

                        <td class="search-label">
                            Form No
                        </td>

                        <td>
                            <input type="text"
                                   name="formNo"
                                   value="45"
                                   class="search-textbox"/>
                        </td>

                        <td></td>
                        <td></td>
                        <td></td>

                        <td>
                            <input type="submit"
                                   value="Refresh"
                                   class="search-btn"/>
                        </td>

                    </tr>

                </table>

            </form>

            <!-- =========================================
                 ACKNOWLEDGEMENT DETAILS
            ========================================== -->

            <div class="section-header">
                <span class="expand-icon">⊖</span>
                ROD Acknowledgement Details
            </div>

            <div class="table-wrapper">

                <table class="details-table">

                    <thead>

                        <tr>

                            <th>Shipping Bill No</th>
                            <th>Shipping Bill Date</th>
                            <th>Port Code</th>
                            <th>Form No</th>
                            <th>IFSC</th>
                            <th>AD Code</th>
                            <th>Export Agency</th>
                            <th>Export Type</th>
                            <th>Receiver Indicator</th>
                            <th>LEO Date</th>
                            <th>Changed IE Code</th>
                            <th>Direct Dispatch Indicator</th>
                            <th>AD IFSC</th>
                            <th>Date of ROD</th>
                            <th>Buyer Name</th>
                            <th>Buyer Country</th>
                            <th>Error Code</th>

                        </tr>

                    </thead>

                    <tbody>

                        <tr>

                            <td colspan="17" class="no-record">
                                No records found
                            </td>

                        </tr>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

</div>

</body>

</html>