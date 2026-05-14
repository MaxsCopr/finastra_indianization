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
<link rel="stylesheet" href="css/dashboard.css" />
<link rel="stylesheet" href="css/bootstrap.css" />
<link rel="stylesheet" href="css/font-awesome.css" />

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

<!--     <div class="logo-left">
        <img src="images/logo.png" alt="Logo"/>
    </div> -->

    <div class="logo-right">
        <img src="images/finastra-logo.png" alt="Union Bank"/>
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
                        Application
                </td>
            </tr>

            <tr>
                <td>
                    <a href="FileUploadView">
                        EDPMS Upload Inward Files
                    </a>
                </td>
            </tr>

            <tr>
                <td>
                    <a href="edpmsSearch">
                        EDPMS Utilize Search
                    </a>
                </td>
            </tr>

            <tr>
                <td>
                    <a href="mdfUnUntilizeAction">
                        SB Utilize Search
                    </a>
                </td>
            </tr>

            <tr>
                <td>
                    <a href="sbUnUntilizeAction">
                        SB Outstanding Payment Search
                    </a>
                </td>
            </tr>

            <tr>
                <td>
                    <a href="adTransfer">
                        AD Transfer
                    </a>
                </td>
            </tr>

            <tr>
                <td>
                    <a href="acknowledgement">
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