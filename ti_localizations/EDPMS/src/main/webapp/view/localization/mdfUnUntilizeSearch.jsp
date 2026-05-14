<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="boe" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>SB Utilize Search</title>

<link rel="stylesheet" href="css/dashboard.css" />
<link rel="stylesheet" href="css/bootstrap.css" />
<link rel="stylesheet" href="css/font-awesome.css" />
<link rel="stylesheet" type="text/css" href="css/sbUtilizeSearch.css" />

<script type="text/javascript">

function windowClose()
{
    top.close();
}

function resetForm()
{
    document.getElementById("searchForm").reset();
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

            <a href="home">Close</a>

    </div>

    <div style="margin-top:10px; text-align:center;">

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

        <div class="search-panel">

            <!-- =========================================
                 SEARCH HEADER
            ========================================== -->

            <div class="section-header">
                <span class="expand-icon">⊖</span>
                SB Utilize Search
            </div>

            <!-- =========================================
                 SEARCH FORM
            ========================================== -->

            <form id="searchForm"
                  action="sbUtilizeSearch"
                  method="post">

                <table class="search-form-table">

                    <tr>

                        <td class="search-label">
                            SB Created From
                        </td>

                        <td>
                            <input type="text"
                                   name="sbCreatedFrom"
                                   class="search-textbox"/>
                        </td>

                        <td class="search-label">
                            To
                        </td>

                        <td>
                            <input type="text"
                                   name="sbCreatedTo"
                                   class="search-textbox"/>
                        </td>

                        <td class="search-label">
                            Shipping Bill No
                        </td>

                        <td>
                            <input type="text"
                                   name="shippingBillNo"
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
                 SHIPPING DETAILS
            ========================================== -->

            <div class="section-header">
                <span class="expand-icon">⊖</span>
                Shipping Details
            </div>

            <div class="shipping-table-wrapper">

                <table class="shipping-table">

                    <thead>

                        <tr>

                            <th>Shipping Bill No</th>
                            <th>Shipping Bill Date</th>
                            <th>Port Code</th>
                            <th>Form No</th>
                            <th>IFSC</th>
                            <th>ADCode</th>
                            <th>Export Type</th>
                            <th>Export Agency</th>
                            <th>Receiver Indicator</th>
                            <th>Invoice No</th>
                            <th>Invoice Amount</th>
                            <th>FOB Date</th>
                            <th>FOB Currency</th>
                            <th>FOB Amount</th>
                            <th>Freight Currency</th>
                            <th>Freight Amount</th>
                            <th>Insurance Currency</th>
                            <th>Insurance Amount</th>

                        </tr>

                    </thead>

                    <tbody>

                        <tr>

                            <td colspan="18" class="no-record">
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