<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>EDPMS Utilize Search</title>

<link rel="stylesheet" href="css/jquery-ui.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/datepicker.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-dropdown.css"/>
<link type="text/css" rel="stylesheet" href="css/headfoot.css" />
<link href="css/font-awesome.css" rel="stylesheet" />

<style>

body {
    background: #052b67;
    font-family: Arial, Helvetica, sans-serif;
    margin: 0;
    padding: 0;
}

.main-wrapper {
    width: 100%;
}

.header-section {
    background: #052b67;
    height: 90px;
}

.left-panel {
    width: 160px;
    float: left;
    background: #052b67;
    min-height: 700px;
    padding-top: 20px;
}
.logo-right img{
    width: 200px;
    height: 41px;
}

.left-panel .btn-custom {
    width: 120px;
    margin: 10px auto;
    display: block;
    background: #d9d9d9;
    border: 1px solid #999;
    height: 24px;
    font-size: 12px;
    line-height: 12px;
}

.content-panel {
    margin-left: 160px;
    background: #f3f3f3;
    min-height: 700px;
    padding: 10px;
}

.section-title {
    background: #d9edf7;
    border: 1px solid #bcdff1;
    color: #1b4f72;
    font-weight: bold;
    padding: 5px;
    font-size: 12px;
    margin-bottom: 10px;
}

.form-table {
    width: 100%;
    font-size: 11px;
}

.form-table td {
    padding: 4px;
}

.form-control-custom {
    width: 180px;
    height: 22px;
    border: 1px solid #b5b5b5;
    font-size: 11px;
}

.refresh-btn {
    background: #1d7fd0;
    color: white;
    border: none;
    padding: 2px 10px;
    font-size: 11px;
}

.grid-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 10px;
    margin-top: 5px;
}

.grid-table th {
    border: 1px solid #cfcfcf;
    background: #efefef;
    padding: 4px;
    text-align: center;
}

.grid-table td {
    border: 1px solid #cfcfcf;
    padding: 4px;
    height: 25px;
}

.logo-box {
    width: 60px;
    height: 60px;
    background: white;
    margin: 10px auto;
}

.clearfix::after {
    content: "";
    display: block;
    clear: both;
}

</style>

<script>

function windowClose() {
    top.close();
}

function resetForm() {
    document.getElementById("searchForm").reset();
}

</script>

</head>

<body>

<div class="main-wrapper">

    <!-- HEADER -->
    <div class="header-section clearfix">

    <div class="logo-right">
        <img src="images/finastra-logo.png" alt="Union Bank"/>
    </div>

    </div>

    <!-- LEFT PANEL -->
    <div class="left-panel">

            <button class="btn-custom">
               <a href="home">Close</a>
            </button>

        <button class="btn-custom" onclick="resetForm()">
            Reset
        </button>

    </div>

    <!-- CONTENT PANEL -->
    <div class="content-panel">

        <!-- SEARCH SECTION -->
        <div class="section-title">
            EDPMS Utilize Search
        </div>

        <form id="searchForm">

            <table class="form-table">

                <tr>
                    <td><b>Bill Reference No</b></td>
                    <td>
                        <input type="text"
                               name="billRefNo"
                               class="form-control-custom"/>
                    </td>

                    <td><b>Event Reference No</b></td>
                    <td>
                        <input type="text"
                               name="eventRefNo"
                               class="form-control-custom"/>
                    </td>
                </tr>

                <tr>
                    <td><b>Customer CIF No</b></td>
                    <td>
                        <input type="text"
                               name="customerCifNo"
                               class="form-control-custom"/>
                    </td>

                    <td><b>Shipping Bill No</b></td>
                    <td>
                        <input type="text"
                               name="shippingBillNo"
                               class="form-control-custom"/>
                    </td>
                </tr>

                <tr>
                    <td><b>Shipping Bill Date</b></td>
                    <td>
                        <input type="text"
                               name="shippingBillDate"
                               class="form-control-custom"/>
                    </td>

                    <td><b>Port Code</b></td>
                    <td>
                        <input type="text"
                               name="portCode"
                               class="form-control-custom"/>
                    </td>
                </tr>

                <tr>
                    <td><b>IE Code</b></td>
                    <td>
                        <input type="text"
                               name="ieCode"
                               class="form-control-custom"/>
                    </td>

                    <td><b>Form No</b></td>
                    <td>
                        <input type="text"
                               name="formNo"
                               class="form-control-custom"/>
                    </td>
                </tr>

                <tr>
                    <td colspan="4">
                        <button type="button" class="refresh-btn">
                            Refresh
                        </button>
                    </td>
                </tr>

            </table>

        </form>

        <!-- SHIPPING DETAILS -->
        <div class="section-title">
            Shipping Details
        </div>

        <div style="overflow-x:auto;">

            <table class="grid-table">

                <tr>
                    <th>Bill Ref No</th>
                    <th>Event Ref No</th>
                    <th>Customer Name</th>
                    <th>Shipping Bill No</th>
                    <th>Shipping Bill Date</th>
                    <th>Port Code</th>
                    <th>Form No</th>
                    <th>IE Code</th>
                    <th>Customer Code</th>
                    <th>AD Code</th>
                    <th>Country</th>
                    <th>Export Agency</th>
                    <th>Export Type</th>
                    <th>ROD Status</th>
                    <th>PEN Status</th>
                    <th>GR Type</th>
                </tr>

                <tr>
                    <td colspan="16" align="center">
                        No records found
                    </td>
                </tr>

            </table>

        </div>

        <!-- INVOICE DETAILS -->
        <div class="section-title">
            Invoice Details
        </div>

        <div style="overflow-x:auto;">

            <table class="grid-table">

                <tr>
                    <th>Bill Ref No</th>
                    <th>Event Ref No</th>
                    <th>Invoice Serial No</th>
                    <th>Invoice Date</th>
                    <th>Invoice Total</th>
                    <th>Invoice Currency</th>
                    <th>FOB Amount</th>
                    <th>Freight Currency</th>
                    <th>Freight Amount</th>
                    <th>Insurance Currency</th>
                    <th>Insurance Amount</th>
                    <th>Commission Currency</th>
                    <th>Commission Amount</th>
                    <th>Discount Currency</th>
                    <th>Discount Amount</th>
                    <th>Deduction Currency</th>
                    <th>Deduction Amount</th>
                </tr>

                <tr>
                    <td colspan="17" align="center">
                        No records found
                    </td>
                </tr>

            </table>

        </div>

    </div>

</div>

</body>
</html>