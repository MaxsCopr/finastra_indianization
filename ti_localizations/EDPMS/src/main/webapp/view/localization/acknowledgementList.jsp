<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="boe" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>GR Acknowledgement</title>

<link rel="stylesheet" href="css/dashboard.css" />
<link rel="stylesheet" type="text/css"
      href="css/acknowledgement.css"/>

<script type="text/javascript">

function windowClose()
{
    top.close();
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

    <div class="left-line"></div>

</div>

<!-- =====================================================
     MAIN CONTENT
===================================================== -->

<div class="main-content">

    <div class="content-box">

        <div class="ack-panel">

            <!-- =========================================
                 TITLE
            ========================================== -->

            <div class="section-header">
                <span class="expand-icon">⊖</span>
                GR Acknowledgement List
            </div>

            <!-- =========================================
                 ACKNOWLEDGEMENT LINKS
            ========================================== -->

            <div class="ack-list">

                <a href="rodAckAction"
                   class="ack-link">

                    Receipt of Document Acknowledgement

                </a>

                <a href="penAckAction"
                   class="ack-link">

                    Payment Extension Acknowledgement

                </a>

                <a href="prnAckAction"
                   class="ack-link">

                    Payment Realization Acknowledgement

                </a>

                <a href="trrAckAction"
                   class="ack-link">

                    AD-Transfer Acknowledgement

                </a>

                <a href="writeOffSetOffAcknowledgement"
                   class="ack-link">

                    WriteOff/SetOff Acknowledgement

                </a>

                <a href="irmAcknowledgement"
                   class="ack-link">

                    IRM Acknowledgement

                </a>

                <a href="irpIrcAcknowledgement"
                   class="ack-link">

                    IRP IRC Acknowledgement

                </a>

            </div>

        </div>

    </div>

</div>

</body>

</html>