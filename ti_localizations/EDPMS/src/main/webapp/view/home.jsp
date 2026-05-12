<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="boe" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>Home Page</title>

<link rel="stylesheet" href="css/bootstrap.css" />
<link rel="stylesheet" href="css/font-awesome.css" />

<style>

body{
    margin:0;
    padding:0;
    background:#062b67;
    font-family: Arial, Helvetica, sans-serif;
}

/* ================= HEADER ================= */

.header{
    width:100%;
    height:85px;
    background:#062b67;
    position:relative;
}

.logo-left{
    position:absolute;
    left:10px;
    top:10px;
}

.logo-left img{
    width:70px;
    height:70px;
}

.logo-right{
    position:absolute;
    right:15px;
    top:10px;
}

.logo-right img{
    width:75px;
    height:55px;
}

/* ================= LEFT MENU ================= */

.left-panel{
    width:210px;
    min-height:500px;
    background:#062b67;
    float:left;
    color:#fff;
}

.close-btn{
    margin-top:20px;
    width:100%;
    text-align:center;
}

.close-btn a{
    display:block;
    width:180px;
    margin:auto;
    padding:4px;
    background:#d9d9d9;
    color:#333;
    text-decoration:none;
    font-size:12px;
    border:1px solid #bcbcbc;
}

.left-line{
    border-bottom:1px solid #9bb0d1;
    margin-top:8px;
}

/* ================= CONTENT ================= */

.main-content{
    margin-left:220px;
    padding:20px;
}

.content-box{
    background:#f5f5f5;
    border:1px solid #d5d5d5;
    min-height:430px;
    padding:10px;
}

.section-title{
    background:#d9edf7;
    color:#1d6f8d;
    font-size:12px;
    font-weight:bold;
    padding:4px 10px;
    border:1px solid #c5dbe5;
}

.menu-table{
    width:100%;
    border-collapse:collapse;
    margin-top:5px;
}

.menu-table tr{
    height:24px;
}

.menu-table tr:nth-child(odd){
    background:#f2f2f2;
}

.menu-table tr:nth-child(even){
    background:#e6e6e6;
}

.menu-table td{
    padding-left:12px;
    font-size:11px;
    color:#333;
}

.menu-table a{
    text-decoration:none;
    color:#333;
    display:block;
}

.menu-table a:hover{
    color:#0056b3;
    text-decoration:underline;
}

.footer-space{
    clear:both;
    height:40px;
}

</style>

<script type="text/javascript">

function windowClose()
{
    top.close();
}

</script>

</head>

<body>

<!-- ================= HEADER ================= -->

<div class="header">

    <div class="logo-left">
        <img src="images/logo.png" alt="Logo"/>
    </div>

    <div class="logo-right">
        <img src="images/unionbank.png" alt="Union Bank"/>
    </div>

</div>

<!-- ================= LEFT SIDE ================= -->

<div class="left-panel">

    <div class="close-btn">

        <boe:if test="%{#session.xEvtRefNum.length() > 0}">
            <a href="#" onclick="windowClose()">Close</a>
        </boe:if>

        <boe:else>
            <a href="closeBOEWindow">Close</a>
        </boe:else>

    </div>

    <div class="left-line"></div>

</div>

<!-- ================= MAIN CONTENT ================= -->

<div class="main-content">

    <div class="content-box">

        <div class="section-title">
            GR Process
        </div>

        <table class="menu-table">

            <tr>
                <td>
                    <a href="BillEnteryForm">
                        Application
                    </a>
                </td>
            </tr>

            <tr>
                <td>
                    <a href="billNoToManyPaymentReference">
                        EDPMS Upload Inward Files
                    </a>
                </td>
            </tr>

            <tr>
                <td>
                    <a href="manualBOE">
                        EDPMS Utilize Search
                    </a>
                </td>
            </tr>

            <tr>
                <td>
                    <a href="viewRejectedTransactions">
                        SB Utilize Search
                    </a>
                </td>
            </tr>

            <tr>
                <td>
                    <a href="BOEBlk">
                        SB Outstanding Payment Search
                    </a>
                </td>
            </tr>

            <tr>
                <td>
                    <a href="#">
                        AD Transfer
                    </a>
                </td>
            </tr>

            <tr>
                <td>
                    <a href="#">
                        GR Acknowledgement
                    </a>
                </td>
            </tr>

            <tr>
                <td>
                    <a href="#">
                        FRN Cancel Request
                    </a>
                </td>
            </tr>

            <tr>
                <td>
                    <a href="#">
                        EDPMS File Download
                    </a>
                </td>
            </tr>

        </table>

    </div>

</div>

<div class="footer-space"></div>

</body>
</html>