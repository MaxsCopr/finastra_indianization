package in.co.FIRC.utility;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import freemarker.log.Logger;
import in.co.FIRC.vo.ourBank.PrintOurBankVO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class BarcodeGenerator
{
  private static Logger logger = Logger.getLogger(BarcodeGenerator.class.getName());
 
  public void writePdf(PrintOurBankVO printVO, ByteArrayOutputStream baos)
    throws Exception
  {
    Document document = new Document(new Rectangle(PageSize.A4));
    PdfWriter.getInstance(document, baos);
   
    ArrayList<PrintOurBankVO> billTypeList = null;
    ArrayList<PrintOurBankVO> accList = null;
    if (printVO != null)
    {
      billTypeList = printVO.getPrintList();
      accList = printVO.getAccList();
    }
    Font fontTitle = new Font(Font.FontFamily.COURIER, 12.0F, 1, BaseColor.BLACK);
    Font fontlet = new Font(Font.FontFamily.COURIER, 10.0F, 1, BaseColor.BLACK);
    Font fontList = new Font(Font.FontFamily.COURIER, 10.0F, 0, BaseColor.BLACK);
   

    Paragraph para = new Paragraph();
    Paragraph para1 = new Paragraph();
    Paragraph para2 = new Paragraph();
    Paragraph para3 = new Paragraph();
    Paragraph para4 = new Paragraph();
    Paragraph para5 = new Paragraph();
    Paragraph para6 = new Paragraph();
    Paragraph para7 = new Paragraph();
    Paragraph para8 = new Paragraph();
    Paragraph para9 = new Paragraph();
    Paragraph para10 = new Paragraph();
    Paragraph para11 = new Paragraph();
    Paragraph para12 = new Paragraph();
    Paragraph para13 = new Paragraph();
    Paragraph para14 = new Paragraph();
    Paragraph para15 = new Paragraph();
    Paragraph para16 = new Paragraph();
    Paragraph para17 = new Paragraph();
    Paragraph para18 = new Paragraph();
    Paragraph para19 = new Paragraph();
    Paragraph para20 = new Paragraph();
   
    para.setFont(fontList);
    para1.setFont(fontList);
    para2.setFont(fontList);
    para3.setFont(fontList);
    para4.setFont(fontList);
    para5.setFont(fontList);
    para6.setFont(fontList);
    para7.setFont(fontList);
    para8.setFont(fontList);
    para9.setFont(fontList);
    para10.setFont(fontList);
    para11.setFont(fontList);
    para12.setFont(fontList);
    para13.setFont(fontList);
    para14.setFont(fontList);
    para15.setFont(fontList);
    para16.setFont(fontList);
    para17.setFont(fontList);
    para18.setFont(fontList);
    para19.setFont(fontList);
    para20.setFont(fontList);
    for (int i = 0; i < billTypeList.size(); i++)
    {
      PrintOurBankVO printOurBankVO = (PrintOurBankVO)billTypeList.get(i);
      if (printOurBankVO != null)
      {
        Chunk d1 = new Chunk("We certify that we have received the following remittance and proceeds thereof were paid to : ");
       
        para.setIndentationLeft(20.0F);
        para.setAlignment(0);
        para.add(d1);
       
        Chunk d2 = new Chunk("(a) To the beneficiary ", fontlet);
        para1.setAlignment(0);
        para1.add(d2);
        para1.setTabSettings(new TabSettings(22.0F));
        para1.add(Chunk.TABBING);
        para1.add(new Chunk("  " + printOurBankVO.getBenificiarydetails()));
       
        Chunk d3 = new Chunk("By Credit to Current/Saving/Cash Credit Account with us " + printVO.getAccNo());
        para2.setIndentationLeft(20.0F);
        para2.setAlignment(0);
        para2.add(d3);
       
        Chunk d11 = new Chunk("DATE OF CREDIT :  ");
        para18.setIndentationLeft(20.0F);
        para18.setAlignment(0);
        para18.add(d11);
        para18.setTabSettings(new TabSettings(22.0F));
        para18.add(Chunk.TABBING);
        para18.add(new Chunk("  " + printOurBankVO.getValue_date()));
       
        Chunk d4 = new Chunk("(b) To Bank on for credit of beneficiary's Account ", fontlet);
        para3.setAlignment(0);
        para3.add(d4);
       
        Chunk remName = new Chunk("Name and Place of Residence of Remitter ");
        para4.setIndentationLeft(20.0F);
        para4.setAlignment(0);
        para4.add(remName);
        para4.add(Chunk.TABBING);
        para4.add(new Chunk(
          ":   " + printOurBankVO.getOrderingcustomer() + " " + printOurBankVO.getRem_country()));
       
        Chunk remBank = new Chunk("Name and Place of Remitting bank ");
        para5.setIndentationLeft(20.0F);
        para5.setAlignment(0);
        para5.add(remBank);
        para5.setTabSettings(new TabSettings(126.0F));
        para5.add(Chunk.TABBING);
        para5.add(new Chunk(":   " + printOurBankVO.getRemmitngbankdetails()));
       
        Chunk mtNo = new Chunk("DD/TT/MT No ");
        para6.setIndentationLeft(20.0F);
        para6.setAlignment(0);
        para6.add(mtNo);
        para6.setTabSettings(new TabSettings(50.0F));
        para6.add(Chunk.TABBING);
        para6.add(new Chunk(
          ":   " + printOurBankVO.getNostroNo() + "  Dated  : " + printOurBankVO.getNostroDate()));
       
        Chunk foreignCurr = new Chunk("Foreign Currency Amount ", fontlet);
        para7.setIndentationLeft(20.0F);
        para7.setAlignment(0);
        para7.add(foreignCurr);
       
        int count = 0;
        double amount = 0.0D;
        String amountString = "";
        for (PrintOurBankVO printAccVal : accList) {
          if (!printAccVal.getFircEX().equals("1"))
          {
            amount += Double.parseDouble(printAccVal.getFircConAmt());
            amountString = getConvertedAmount(Double.valueOf(amount)).toString();
            count++;
          }
        }
        String equivalentStr = "";
        if (count > 0) {
          equivalentStr = " Equivalent to : INR " + getConvertedAmount(Double.valueOf(amount));
        }
        for (PrintOurBankVO printCurrList : accList)
        {
          logger.info("getFircCurrRec() :" + printCurrList.getFircCurrRec());
          logger.info("getFircCurr() :" + printCurrList.getFircCurr());
          if (printCurrList.getFircCurr() != null) {
            if (!printCurrList.getFircCurr().equals("INR"))
            {
              para7.setTabSettings(new TabSettings(20.0F));
              para7.add(Chunk.TABBING);
              para7.add(new Chunk(printOurBankVO.getCurrency() + "  " + printOurBankVO.getAmount() +
                " and EEFC Amount " + printOurBankVO.getAmount() + " \n " + equivalentStr));
            }
            else
            {
              para7.setTabSettings(new TabSettings(20.0F));
              para7.add(Chunk.TABBING);
              para7.add(new Chunk(printOurBankVO.getCurrency() + "  " + printOurBankVO.getAmount() +
                " and EEFC Amount 0.00" + " \n " + equivalentStr));
            }
          }
        }
        Chunk currencyDetails = new Chunk("Currency Conversion Details are as below :");
        para19.setIndentationLeft(20.0F);
        para19.setAlignment(0);
        para19.add(currencyDetails);
       
        PdfPTable table = new PdfPTable(5);
       
        table.setWidthPercentage(90.0F);
       
        PdfPCell cell1 = new PdfPCell(new Phrase("From Currency", fontList));
        cell1.setPadding(5.0F);
        cell1.setHorizontalAlignment(0);
        table.addCell(cell1);
       
        PdfPCell cell2 = new PdfPCell(new Phrase("Amount", fontList));
        cell2.setPadding(5.0F);
        cell2.setHorizontalAlignment(0);
        table.addCell(cell2);
       
        PdfPCell cell3 = new PdfPCell(new Phrase("Rate", fontList));
        cell3.setPadding(5.0F);
        cell3.setHorizontalAlignment(0);
        table.addCell(cell3);
       
        PdfPCell cell4 = new PdfPCell(new Phrase("To Currency", fontList));
        cell4.setPadding(5.0F);
        cell4.setHorizontalAlignment(0);
        table.addCell(cell4);
       
        PdfPCell cell5 = new PdfPCell(new Phrase("Amount", fontList));
        cell5.setPadding(5.0F);
        cell5.setHorizontalAlignment(0);
        table.addCell(cell5);
        for (int k = 0; k < accList.size(); k++)
        {
          PrintOurBankVO printAccVal = (PrintOurBankVO)accList.get(k);
          PdfPCell columnCell1 = new PdfPCell(new Phrase(printAccVal.getFircCurrRec(), fontList));
          columnCell1.setPadding(5.0F);
          columnCell1.setHorizontalAlignment(0);
          table.addCell(columnCell1);
         
          PdfPCell columnCell2 = new PdfPCell(new Phrase(printAccVal.getFircAmount(), fontList));
          columnCell2.setPadding(5.0F);
          columnCell2.setHorizontalAlignment(0);
          table.addCell(columnCell2);
         
          PdfPCell columnCell3 = new PdfPCell(new Phrase(printAccVal.getFircEX(), fontList));
          columnCell3.setPadding(5.0F);
          columnCell3.setHorizontalAlignment(0);
          table.addCell(columnCell3);
         
          PdfPCell columnCell4 = new PdfPCell(new Phrase(printAccVal.getFircCurr(), fontList));
          columnCell4.setPadding(5.0F);
          columnCell4.setHorizontalAlignment(0);
          table.addCell(columnCell4);
         
          PdfPCell columnCell5 = new PdfPCell(new Phrase(amountString, fontList));
          columnCell5.setPadding(5.0F);
          columnCell5.setHorizontalAlignment(0);
          table.addCell(columnCell5);
        }
        float[] colWidths = { 20.0F, 25.0F, 15.0F, 25.0F, 25.0F };
        table.setWidths(colWidths);
       
        Chunk purpose = new Chunk("Purpose of remittance as per Beneficiary / Remitter : ", fontlet);
        para11.setIndentationLeft(20.0F);
        para11.setAlignment(0);
        para11.add(purpose);
        para11.add(new Chunk(printOurBankVO.getPurposedesc()));
       
        Chunk payCertify = new Chunk("We also certify that the payment thereof has / has not been received in non-convertible rupees or under any special trade or payments agreement.");
       
        para12.setIndentationLeft(20.0F);
        para12.setAlignment(0);
        para12.add(payCertify);
       
        Chunk appManner = new Chunk("We confirm that we have obtained reimbursement in an approved manner.");
        para13.setIndentationLeft(20.0F);
        para13.setAlignment(0);
        para13.add(appManner);
       
        Chunk certificate = new Chunk("Purpose if advance export then the shipment of goods should be made within one year from date of receipt of advance payment.");
       
        para14.setIndentationLeft(20.0F);
        para14.setAlignment(0);
        para14.add(certificate);
       
        Chunk lastLine = new Chunk(
          "This is system generated advice and no signature is required.");
        para15.setIndentationLeft(20.0F);
        para15.setAlignment(0);
        para15.add(lastLine);
       
        Chunk ourBank = new Chunk("KOTAK MAHINDRA BANK LTD\n" + printOurBankVO.getAddress1() + "\n" +
          printOurBankVO.getAddress2() + "\n" + printOurBankVO.getAddress3() + "\n" +
          printOurBankVO.getAddress4() + "\n" + printOurBankVO.getAddress5(), fontlet);
        para16.setIndentationLeft(20.0F);
        para16.setAlignment(0);
        para16.add(ourBank);
       
        Chunk linebreak = new Chunk(new DottedLineSeparator());
       
        document.open();
       
        float[] columnWidths1 = { 10.0F };
       
        PdfPTable table1 = new PdfPTable(columnWidths1);
        table1.setWidthPercentage(100.0F);
        table1.getDefaultCell().setBorder(0);
        table1.getDefaultCell().setHorizontalAlignment(1);
        table1.addCell(para16);
        document.add(table1);
       
        document.add(linebreak);
       
        float[] columnWidths = { 10.0F, 10.0F };
        PdfPTable table2 = new PdfPTable(columnWidths);
        table2.setWidthPercentage(100.0F);
        table2.getDefaultCell().setBorder(0);
        table2.getDefaultCell().setHorizontalAlignment(0);
        Phrase firstLineLeft = new Phrase("Ref No. : " + printOurBankVO.getTransrefno(), fontTitle);
        table2.addCell(firstLineLeft);
        table2.getDefaultCell().setHorizontalAlignment(2);
        Phrase firstLineRight = new Phrase("Date : " + printOurBankVO.getSystemDate(), fontTitle);
        table2.addCell(firstLineRight);
       
        document.add(table2);
       
        Chunk title = new Chunk("CERTIFICATE OF FOREIGN INWARD REMITTANCE", fontlet);
        para17.setIndentationLeft(20.0F);
        para17.setAlignment(0);
        para17.add(title);
       
        PdfPTable table3 = new PdfPTable(columnWidths1);
        table3.setWidthPercentage(100.0F);
        table3.getDefaultCell().setBorder(0);
        table3.getDefaultCell().setHorizontalAlignment(1);
        table3.addCell(para17);
        document.add(table3);
       
        document.add(para);
       
        document.add(para1);
        document.add(para2);
       
        document.add(para18);
       
        document.add(Chunk.NEWLINE);
        document.add(para3);
        document.add(para4);
        document.add(para5);
        document.add(para6);
        document.add(para7);
       
        document.add(para19);
        document.add(Chunk.NEWLINE);
        document.add(table);
       
        document.add(Chunk.NEWLINE);
        document.add(para11);
        document.add(Chunk.NEWLINE);
        document.add(para12);
        document.add(Chunk.NEWLINE);
        document.add(para13);
        document.add(Chunk.NEWLINE);
        document.add(para14);
        document.add(Chunk.NEWLINE);
        document.add(para15);
       


































        document.close();
      }
    }
  }
 
  public void writeDupPdf(PrintOurBankVO printVO, ByteArrayOutputStream baos)
    throws Exception
  {
    logger.info("-----------------writeDupPdf--------------------");
   

    Document document = new Document(new Rectangle(PageSize.A4));
    PdfWriter.getInstance(document, baos);
   
    ArrayList<PrintOurBankVO> billTypeList = null;
    ArrayList<PrintOurBankVO> accList = null;
    if (printVO != null)
    {
      billTypeList = printVO.getPrintList();
      accList = printVO.getAccList();
    }
    Font fontTitle = new Font(Font.FontFamily.COURIER, 12.0F, 1, BaseColor.BLACK);
    Font fontlet = new Font(Font.FontFamily.COURIER, 10.0F, 1, BaseColor.BLACK);
    Font fontList = new Font(Font.FontFamily.COURIER, 10.0F, 0, BaseColor.BLACK);
   

    Paragraph para = new Paragraph();
    Paragraph para1 = new Paragraph();
    Paragraph para2 = new Paragraph();
    Paragraph para3 = new Paragraph();
    Paragraph para4 = new Paragraph();
    Paragraph para5 = new Paragraph();
    Paragraph para6 = new Paragraph();
    Paragraph para7 = new Paragraph();
    Paragraph para8 = new Paragraph();
    Paragraph para9 = new Paragraph();
    Paragraph para10 = new Paragraph();
    Paragraph para11 = new Paragraph();
    Paragraph para12 = new Paragraph();
    Paragraph para13 = new Paragraph();
    Paragraph para14 = new Paragraph();
    Paragraph para15 = new Paragraph();
    Paragraph para16 = new Paragraph();
    Paragraph para17 = new Paragraph();
    Paragraph para18 = new Paragraph();
    Paragraph para19 = new Paragraph();
    Paragraph para20 = new Paragraph();
   
    para.setFont(fontList);
    para1.setFont(fontList);
    para2.setFont(fontList);
    para3.setFont(fontList);
    para4.setFont(fontList);
    para5.setFont(fontList);
    para6.setFont(fontList);
    para7.setFont(fontList);
    para8.setFont(fontList);
    para9.setFont(fontList);
    para10.setFont(fontList);
    para11.setFont(fontList);
    para12.setFont(fontList);
    para13.setFont(fontList);
    para14.setFont(fontList);
    para15.setFont(fontList);
    para16.setFont(fontList);
    para17.setFont(fontList);
    para18.setFont(fontList);
    para19.setFont(fontList);
    para20.setFont(fontList);
    for (int i = 0; i < billTypeList.size(); i++)
    {
      PrintOurBankVO printOurBankVO = (PrintOurBankVO)billTypeList.get(i);
      if (printOurBankVO != null)
      {
        Chunk d1 = new Chunk("We certify that we have received the following remittance and proceeds thereof were paid to : ");
       
        para.setIndentationLeft(20.0F);
        para.setAlignment(0);
        para.add(d1);
       
        Chunk d2 = new Chunk("(a) To the beneficiary ", fontlet);
        para1.setAlignment(0);
        para1.add(d2);
        para1.setTabSettings(new TabSettings(22.0F));
        para1.add(Chunk.TABBING);
        para1.add(new Chunk("  " + printOurBankVO.getBenificiarydetails()));
       
        Chunk d3 = new Chunk("By Credit to Current/Saving/Cash Credit Account with us " + printVO.getAccNo());
        para2.setIndentationLeft(20.0F);
        para2.setAlignment(0);
        para2.add(d3);
       
        Chunk d11 = new Chunk("DATE OF CREDIT :  ");
        para18.setIndentationLeft(20.0F);
        para18.setAlignment(0);
        para18.add(d11);
        para18.setTabSettings(new TabSettings(22.0F));
        para18.add(Chunk.TABBING);
        para18.add(new Chunk("  " + printOurBankVO.getValue_date()));
       
        Chunk d4 = new Chunk("(b) To Bank on for credit of beneficiary's Account ", fontlet);
        para3.setAlignment(0);
        para3.add(d4);
       
        Chunk remName = new Chunk("Name and Place of Residence of Remitter ");
        para4.setIndentationLeft(20.0F);
        para4.setAlignment(0);
        para4.add(remName);
        para4.add(Chunk.TABBING);
        para4.add(new Chunk(
          ":   " + printOurBankVO.getOrderingcustomer() + " " + printOurBankVO.getRem_country()));
       
        Chunk remBank = new Chunk("Name and Place of Remitting bank ");
        para5.setIndentationLeft(20.0F);
        para5.setAlignment(0);
        para5.add(remBank);
        para5.setTabSettings(new TabSettings(126.0F));
        para5.add(Chunk.TABBING);
        para5.add(new Chunk(":   " + printOurBankVO.getRemmitngbankdetails()));
       
        Chunk mtNo = new Chunk("DD/TT/MT No ");
        para6.setIndentationLeft(20.0F);
        para6.setAlignment(0);
        para6.add(mtNo);
        para6.setTabSettings(new TabSettings(50.0F));
        para6.add(Chunk.TABBING);
        para6.add(new Chunk(
          ":   " + printOurBankVO.getNostroNo() + "  Dated  : " + printOurBankVO.getNostroDate()));
       
        Chunk foreignCurr = new Chunk("Foreign Currency Amount ", fontlet);
        para7.setIndentationLeft(20.0F);
        para7.setAlignment(0);
        para7.add(foreignCurr);
        String amountString = "";
        int count = 0;
        double amount = 0.0D;
        for (PrintOurBankVO printAccVal : accList) {
          if (!printAccVal.getFircEX().equals("1"))
          {
            amount += Double.parseDouble(printAccVal.getFircConAmt());
            amountString = getConvertedAmount(Double.valueOf(amount)).toString();
            count++;
          }
        }
        String equivalentStr = "";
        logger.info("count :" + count);
        if (count > 0) {
          equivalentStr = " Equivalent to : INR " + getConvertedAmount(Double.valueOf(amount));
        }
        for (PrintOurBankVO printCurrList : accList)
        {
          logger.info("getFircCurrRec() :" + printCurrList.getFircCurrRec());
          logger.info("getFircCurr() :" + printCurrList.getFircCurr());
          if (printCurrList.getFircCurr() != null) {
            if (!printCurrList.getFircCurr().equals("INR"))
            {
              para7.setTabSettings(new TabSettings(20.0F));
              para7.add(Chunk.TABBING);
              para7.add(new Chunk(printOurBankVO.getCurrency() + "  " + printOurBankVO.getAmount() +
                " and EEFC Amount " + printOurBankVO.getAmount() + " \n " + equivalentStr));
            }
            else
            {
              para7.setTabSettings(new TabSettings(20.0F));
              para7.add(Chunk.TABBING);
              para7.add(new Chunk(printOurBankVO.getCurrency() + "  " + printOurBankVO.getAmount() +
                " and EEFC Amount 0.00" + " \n " + equivalentStr));
            }
          }
        }
        Chunk currencyDetails = new Chunk("Currency Conversion Details are as below :");
        para19.setIndentationLeft(20.0F);
        para19.setAlignment(0);
        para19.add(currencyDetails);
       
        PdfPTable table = new PdfPTable(5);
       
        table.setWidthPercentage(90.0F);
       
        PdfPCell cell1 = new PdfPCell(new Phrase("From Currency", fontList));
        cell1.setPadding(5.0F);
        cell1.setHorizontalAlignment(0);
        table.addCell(cell1);
       
        PdfPCell cell2 = new PdfPCell(new Phrase("Amount", fontList));
        cell2.setPadding(5.0F);
        cell2.setHorizontalAlignment(0);
        table.addCell(cell2);
       
        PdfPCell cell3 = new PdfPCell(new Phrase("Rate", fontList));
        cell3.setPadding(5.0F);
        cell3.setHorizontalAlignment(0);
        table.addCell(cell3);
       
        PdfPCell cell4 = new PdfPCell(new Phrase("To Currency", fontList));
        cell4.setPadding(5.0F);
        cell4.setHorizontalAlignment(0);
        table.addCell(cell4);
       
        PdfPCell cell5 = new PdfPCell(new Phrase("Amount", fontList));
        cell5.setPadding(5.0F);
        cell5.setHorizontalAlignment(0);
        table.addCell(cell5);
        for (int k = 0; k < accList.size(); k++)
        {
          PrintOurBankVO printAccVal = (PrintOurBankVO)accList.get(k);
          PdfPCell columnCell1 = new PdfPCell(new Phrase(printAccVal.getFircCurrRec(), fontList));
          columnCell1.setPadding(5.0F);
          columnCell1.setHorizontalAlignment(0);
          table.addCell(columnCell1);
         
          PdfPCell columnCell2 = new PdfPCell(new Phrase(printAccVal.getFircAmount(), fontList));
          columnCell2.setPadding(5.0F);
          columnCell2.setHorizontalAlignment(0);
          table.addCell(columnCell2);
         
          PdfPCell columnCell3 = new PdfPCell(new Phrase(printAccVal.getFircEX(), fontList));
          columnCell3.setPadding(5.0F);
          columnCell3.setHorizontalAlignment(0);
          table.addCell(columnCell3);
         
          PdfPCell columnCell4 = new PdfPCell(new Phrase(printAccVal.getFircCurr(), fontList));
          columnCell4.setPadding(5.0F);
          columnCell4.setHorizontalAlignment(0);
          table.addCell(columnCell4);
         
          PdfPCell columnCell5 = new PdfPCell(new Phrase(amountString, fontList));
          columnCell5.setPadding(5.0F);
          columnCell5.setHorizontalAlignment(0);
          table.addCell(columnCell5);
        }
        float[] colWidths = { 20.0F, 25.0F, 15.0F, 25.0F, 25.0F };
        table.setWidths(colWidths);
       
        Chunk purpose = new Chunk("Purpose of remittance as per Beneficiary / Remitter : ", fontlet);
        para11.setIndentationLeft(20.0F);
        para11.setAlignment(0);
        para11.add(purpose);
        para11.add(new Chunk(printOurBankVO.getPurposedesc()));
       
        Chunk payCertify = new Chunk("We also certify that the payment thereof has / has not been received in non-convertible rupees or under any special trade or payments agreement.");
       
        para12.setIndentationLeft(20.0F);
        para12.setAlignment(0);
        para12.add(payCertify);
       
        Chunk appManner = new Chunk("We confirm that we have obtained reimbursement in an approved manner.");
        para13.setIndentationLeft(20.0F);
        para13.setAlignment(0);
        para13.add(appManner);
       
        Chunk certificate = new Chunk("Purpose if advance export then the shipment of goods should be made within one year from date of receipt of advance payment.");
       
        para14.setIndentationLeft(20.0F);
        para14.setAlignment(0);
        para14.add(certificate);
       
        Chunk lastLine = new Chunk(
          "This is system generated advice and no signature is required.");
        para15.setIndentationLeft(20.0F);
        para15.setAlignment(0);
        para15.add(lastLine);
       
        Chunk ourBank = new Chunk("KOTAK MAHINDRA BANK LTD\n" + printOurBankVO.getAddress1() + "\n" +
          printOurBankVO.getAddress2() + "\n" + printOurBankVO.getAddress3() + "\n" +
          printOurBankVO.getAddress4() + "\n" + printOurBankVO.getAddress5(), fontlet);
        para16.setIndentationLeft(20.0F);
        para16.setAlignment(0);
        para16.add(ourBank);
       
        Chunk linebreak = new Chunk(new DottedLineSeparator());
       
        document.open();
       
        float[] columnWidths1 = { 10.0F };
       
        PdfPTable table1 = new PdfPTable(columnWidths1);
        table1.setWidthPercentage(100.0F);
        table1.getDefaultCell().setBorder(0);
        table1.getDefaultCell().setHorizontalAlignment(1);
        table1.addCell(para16);
        document.add(table1);
       
        document.add(linebreak);
       
        float[] columnWidths = { 20.0F, 12.0F, 13.0F };
        PdfPTable table2 = new PdfPTable(columnWidths);
        table2.setWidthPercentage(100.0F);
        table2.getDefaultCell().setBorder(0);
        table2.getDefaultCell().setHorizontalAlignment(0);
        Phrase firstLineLeft = new Phrase("Ref No : " + printOurBankVO.getTransrefno(), fontTitle);
        table2.addCell(firstLineLeft);
        table2.getDefaultCell().setHorizontalAlignment(1);
        Phrase firstLineCenter = new Phrase("(Duplicate Copy)", fontTitle);
        table2.addCell(firstLineCenter);
        table2.getDefaultCell().setHorizontalAlignment(2);
        Phrase firstLineRight = new Phrase("Date : " + printOurBankVO.getSystemDate(), fontTitle);
        table2.addCell(firstLineRight);
       
        document.add(table2);
       
        Chunk title = new Chunk("CERTIFICATE OF FOREIGN INWARD REMITTANCE", fontlet);
        para17.setIndentationLeft(20.0F);
        para17.setAlignment(0);
        para17.add(title);
       
        PdfPTable table3 = new PdfPTable(columnWidths1);
        table3.setWidthPercentage(100.0F);
        table3.getDefaultCell().setBorder(0);
        table3.getDefaultCell().setHorizontalAlignment(1);
        table3.addCell(para17);
        document.add(table3);
       
        document.add(para);
       
        document.add(para1);
        document.add(para2);
       
        document.add(para18);
       
        document.add(Chunk.NEWLINE);
        document.add(para3);
        document.add(para4);
        document.add(para5);
        document.add(para6);
        document.add(para7);
       
        document.add(para19);
        document.add(Chunk.NEWLINE);
        document.add(table);
       
        document.add(Chunk.NEWLINE);
        document.add(para11);
        document.add(Chunk.NEWLINE);
        document.add(para12);
        document.add(Chunk.NEWLINE);
        document.add(para13);
        document.add(Chunk.NEWLINE);
        document.add(para14);
        document.add(Chunk.NEWLINE);
        document.add(para15);
       




































        document.close();
      }
    }
  }
 
  public void writeAdvicePdf(PrintOurBankVO printVO, String fileName)
    throws Exception
  {
    logger.info("---------writeAdvicePdf---------------------");
    try
    {
      Document document = new Document(new Rectangle(PageSize.A4));
     
      File f = new File(ActionConstants.FILE_LOCATION + fileName + ".pdf");
      if (f.exists()) {
        f.delete();
      }
      PdfWriter writer = PdfWriter.getInstance(document,
        new FileOutputStream(ActionConstants.FILE_LOCATION + fileName + ".pdf"));
     
      ArrayList<PrintOurBankVO> billTypeList = null;
      ArrayList<PrintOurBankVO> accList = null;
      if (printVO != null)
      {
        billTypeList = printVO.getPrintList();
        accList = printVO.getAccList();
      }
      Font fontTitle = new Font(Font.FontFamily.COURIER, 12.0F, 1, BaseColor.BLACK);
      Font fontlet = new Font(Font.FontFamily.COURIER, 10.0F, 1, BaseColor.BLACK);
      Font fontList = new Font(Font.FontFamily.COURIER, 10.0F, 0, BaseColor.BLACK);
     
      Paragraph para = new Paragraph();
      Paragraph para1 = new Paragraph();
      Paragraph para2 = new Paragraph();
      Paragraph para3 = new Paragraph();
      Paragraph para4 = new Paragraph();
      Paragraph para5 = new Paragraph();
      Paragraph para6 = new Paragraph();
      Paragraph para7 = new Paragraph();
      Paragraph para8 = new Paragraph();
      Paragraph para9 = new Paragraph();
      Paragraph para10 = new Paragraph();
      Paragraph para11 = new Paragraph();
      Paragraph para12 = new Paragraph();
      Paragraph para13 = new Paragraph();
      Paragraph para14 = new Paragraph();
      Paragraph para15 = new Paragraph();
      Paragraph para16 = new Paragraph();
      Paragraph para17 = new Paragraph();
      Paragraph para18 = new Paragraph();
      Paragraph para19 = new Paragraph();
     
      para.setFont(fontList);
      para1.setFont(fontList);
      para2.setFont(fontList);
      para3.setFont(fontList);
      para4.setFont(fontList);
      para5.setFont(fontList);
      para6.setFont(fontList);
      para7.setFont(fontList);
      para8.setFont(fontList);
      para9.setFont(fontList);
      para10.setFont(fontList);
      para11.setFont(fontList);
      para12.setFont(fontList);
      para13.setFont(fontList);
      para14.setFont(fontList);
      para15.setFont(fontList);
      para16.setFont(fontList);
      para17.setFont(fontList);
      para18.setFont(fontList);
      para19.setFont(fontList);
      for (int i = 0; i < billTypeList.size(); i++)
      {
        PrintOurBankVO printOurBankVO = (PrintOurBankVO)billTypeList.get(i);
        if (printOurBankVO != null)
        {
          Chunk d1 = new Chunk("We certify that we have received the following remittance and proceeds thereof were paid to : ");
         
          para.setIndentationLeft(20.0F);
          para.setAlignment(0);
          para.add(d1);
         
          Chunk d2 = new Chunk("(a) To the beneficiary  ", fontlet);
          para1.setAlignment(0);
          para1.add(d2);
          para1.setTabSettings(new TabSettings(22.0F));
          para1.add(Chunk.TABBING);
          para1.add(new Chunk("  " + printOurBankVO.getBenificiarydetails()));
         
          Chunk d3 = new Chunk(
            "By Credit to Current/Saving/Cash Credit Account with us " + printVO.getAccNo());
          para2.setIndentationLeft(20.0F);
          para2.setAlignment(0);
          para2.add(d3);
         
          Chunk d11 = new Chunk("DATE OF CREDIT :  ");
          para18.setIndentationLeft(20.0F);
          para18.setAlignment(0);
          para18.add(d11);
          para18.setTabSettings(new TabSettings(22.0F));
          para18.add(Chunk.TABBING);
          para18.add(new Chunk("  " + printOurBankVO.getValue_date()));
         
          Chunk d4 = new Chunk("(b) To Bank on for credit of beneficiary's Account ", fontlet);
          para3.setAlignment(0);
          para3.add(d4);
         
          Chunk remName = new Chunk("Name and Place of Residence of Remitter ");
          para4.setIndentationLeft(20.0F);
          para4.setAlignment(0);
          para4.add(remName);
          para4.add(Chunk.TABBING);
          para4.add(new Chunk(
            ":   " + printOurBankVO.getOrderingcustomer() + " " + printOurBankVO.getRem_country()));
         
          Chunk remBank = new Chunk("Name and Place of Remitting bank ");
          para5.setIndentationLeft(20.0F);
          para5.setAlignment(0);
          para5.add(remBank);
          para5.setTabSettings(new TabSettings(126.0F));
          para5.add(Chunk.TABBING);
          para5.add(new Chunk(":   " + printOurBankVO.getRemmitngbankdetails()));
         
          Chunk mtNo = new Chunk("DD/TT/MT No ");
          para6.setIndentationLeft(20.0F);
          para6.setAlignment(0);
          para6.add(mtNo);
          para6.setTabSettings(new TabSettings(50.0F));
          para6.add(Chunk.TABBING);
          para6.add(new Chunk(
            ":   " + printOurBankVO.getNostroNo() + "  Dated  : " + printOurBankVO.getNostroDate()));
         
          Chunk foreignCurr = new Chunk("Foreign Currency Amount ", fontlet);
          para7.setIndentationLeft(20.0F);
          para7.setAlignment(0);
          para7.add(foreignCurr);
         
          int count = 0;
          double amount = 0.0D;
          String amountString = "";
          for (PrintOurBankVO printAccVal : accList)
          {
            amount += Double.parseDouble(printAccVal.getFircConAmt());
           
            amountString = getConvertedAmount(Double.valueOf(amount)).toString();
            if (!printAccVal.getFircEX().equals("1")) {
              count++;
            }
          }
          String equivalentStr = "";
          if (count > 0) {
            equivalentStr = " Equivalent to : INR " + getConvertedAmount(Double.valueOf(amount));
          }
          for (PrintOurBankVO printCurrList : accList)
          {
            logger.info("getFircCurrRec() :" + printCurrList.getFircCurrRec());
            logger.info("getFircCurr() :" + printCurrList.getFircCurr());
            if (printCurrList.getFircCurr() != null) {
              if (!printCurrList.getFircCurr().equals("INR"))
              {
                para7.setTabSettings(new TabSettings(20.0F));
                para7.add(Chunk.TABBING);
                para7.add(new Chunk(printOurBankVO.getCurrency() + "  " + printOurBankVO.getAmount() +
                  " and EEFC Amount " + printOurBankVO.getAmount() + " \n " + equivalentStr));
              }
              else
              {
                para7.setTabSettings(new TabSettings(20.0F));
                para7.add(Chunk.TABBING);
                para7.add(new Chunk(printOurBankVO.getCurrency() + "  " + printOurBankVO.getAmount() +
                  " and EEFC Amount 0.00" + " \n " + equivalentStr));
              }
            }
          }
          Chunk currencyDetails = new Chunk("Currency Conversion Details are as below :");
          para19.setIndentationLeft(20.0F);
          para19.setAlignment(0);
          para19.add(currencyDetails);
         
          PdfPTable table = new PdfPTable(5);
         
          table.setWidthPercentage(90.0F);
         
          PdfPCell cell1 = new PdfPCell(new Phrase("From Currency", fontList));
          cell1.setPadding(5.0F);
          cell1.setHorizontalAlignment(0);
          table.addCell(cell1);
         
          PdfPCell cell2 = new PdfPCell(new Phrase("Amount", fontList));
          cell2.setPadding(5.0F);
          cell2.setHorizontalAlignment(0);
          table.addCell(cell2);
         
          PdfPCell cell3 = new PdfPCell(new Phrase("Rate", fontList));
          cell3.setPadding(5.0F);
          cell3.setHorizontalAlignment(0);
          table.addCell(cell3);
         
          PdfPCell cell4 = new PdfPCell(new Phrase("To Currency", fontList));
          cell4.setPadding(5.0F);
          cell4.setHorizontalAlignment(0);
          table.addCell(cell4);
         
          PdfPCell cell5 = new PdfPCell(new Phrase("Amount", fontList));
          cell5.setPadding(5.0F);
          cell5.setHorizontalAlignment(0);
          table.addCell(cell5);
          for (int k = 0; k < accList.size(); k++)
          {
            PrintOurBankVO printAccVal = (PrintOurBankVO)accList.get(k);
            PdfPCell columnCell1 = new PdfPCell(new Phrase(printAccVal.getFircCurrRec(), fontList));
            columnCell1.setPadding(5.0F);
            columnCell1.setHorizontalAlignment(0);
            table.addCell(columnCell1);
           
            PdfPCell columnCell2 = new PdfPCell(new Phrase(printOurBankVO.getAmount(), fontList));
            columnCell2.setPadding(5.0F);
            columnCell2.setHorizontalAlignment(0);
            table.addCell(columnCell2);
           
            PdfPCell columnCell3 = new PdfPCell(new Phrase(printAccVal.getFircEX(), fontList));
            columnCell3.setPadding(5.0F);
            columnCell3.setHorizontalAlignment(0);
            table.addCell(columnCell3);
           
            PdfPCell columnCell4 = new PdfPCell(new Phrase(printAccVal.getFircCurr(), fontList));
            columnCell4.setPadding(5.0F);
            columnCell4.setHorizontalAlignment(0);
            table.addCell(columnCell4);
           
            PdfPCell columnCell5 = new PdfPCell(new Phrase(amountString, fontList));
            columnCell5.setPadding(5.0F);
            columnCell5.setHorizontalAlignment(0);
            table.addCell(columnCell5);
          }
          float[] colWidths = { 20.0F, 25.0F, 15.0F, 15.0F, 25.0F };
          table.setWidths(colWidths);
         
          Chunk purpose = new Chunk("Purpose of remittance as per Beneficiary / Remitter : ", fontlet);
          para11.setIndentationLeft(20.0F);
          para11.setAlignment(0);
          para11.add(purpose);
          para11.add(new Chunk(printOurBankVO.getPurposedesc()));
         
          Chunk payCertify = new Chunk(
            "We also certify that the payment thereof has / has not been received in non-convertible rupees or under any special trade or payments agreement.");
         
          para12.setIndentationLeft(20.0F);
          para12.setAlignment(0);
          para12.add(payCertify);
         
          Chunk appManner = new Chunk(
            "We confirm that we have obtained reimbursement in an approved manner.");
          para13.setIndentationLeft(20.0F);
          para13.setAlignment(0);
          para13.add(appManner);
         
          Chunk certificate = new Chunk("Purpose if advance export then the shipment of goods should be made within one year from date of receipt of advance payment.");
         
          para14.setIndentationLeft(20.0F);
          para14.setAlignment(0);
          para14.add(certificate);
         
          Chunk signature = new Chunk("This is system generated advice and no signature is required.");
          para15.setIndentationLeft(20.0F);
          para15.setAlignment(0);
          para15.add(signature);
         
          Chunk ourBank = new Chunk("KOTAK MAHINDRA BANK LTD\n" + printOurBankVO.getAddress1() + "\n" +
            printOurBankVO.getAddress2() + "\n" + printOurBankVO.getAddress3() + "\n" +
            printOurBankVO.getAddress4() + "\n" + printOurBankVO.getAddress5(), fontlet);
          para16.setIndentationLeft(20.0F);
          para16.setAlignment(0);
          para16.add(ourBank);
         
          Chunk linebreak = new Chunk(new DottedLineSeparator());
         
          document.open();
         
          float[] columnWidths1 = { 10.0F };
         
          PdfPTable table1 = new PdfPTable(columnWidths1);
          table1.setWidthPercentage(100.0F);
          table1.getDefaultCell().setBorder(0);
          table1.getDefaultCell().setHorizontalAlignment(1);
          table1.addCell(para16);
          document.add(table1);
         
          document.add(linebreak);
         
          float[] columnWidths = { 10.0F, 10.0F };
          PdfPTable table2 = new PdfPTable(columnWidths);
          table2.setWidthPercentage(100.0F);
          table2.getDefaultCell().setBorder(0);
          table2.getDefaultCell().setHorizontalAlignment(0);
          Phrase firstLineLeft = new Phrase("Ref No : " + printOurBankVO.getTransrefno(), fontTitle);
          table2.addCell(firstLineLeft);
          table2.getDefaultCell().setHorizontalAlignment(2);
          Phrase firstLineRight = new Phrase("Date : " + printOurBankVO.getSystemDate(), fontTitle);
          table2.addCell(firstLineRight);
         
          document.add(table2);
          document.add(Chunk.NEWLINE);
         
          Chunk title = new Chunk("FOREIGN INWARD REMITTANCE ADVICE", fontlet);
          para17.setIndentationLeft(20.0F);
          para17.setAlignment(0);
          para17.add(title);
         
          PdfPTable table3 = new PdfPTable(columnWidths1);
          table3.setWidthPercentage(100.0F);
          table3.getDefaultCell().setBorder(0);
          table3.getDefaultCell().setHorizontalAlignment(1);
          table3.addCell(para17);
          document.add(table3);
         
          document.add(para);
         
          document.add(para1);
          document.add(para2);
         
          document.add(para18);
         
          document.add(Chunk.NEWLINE);
          document.add(para3);
          document.add(para4);
          document.add(para5);
          document.add(para6);
          document.add(para7);
         
          document.add(para19);
          document.add(Chunk.NEWLINE);
          document.add(table);
         
          document.add(Chunk.NEWLINE);
          document.add(para11);
          document.add(Chunk.NEWLINE);
          document.add(para12);
          document.add(Chunk.NEWLINE);
          document.add(para13);
          document.add(Chunk.NEWLINE);
          document.add(para14);
          document.add(Chunk.NEWLINE);
          document.add(para15);
          document.close();
        }
      }
      writer.close();
      logger.info("Test---------------1234567----");
    }
    catch (Exception e)
    {
      logger.info("---------writeAdvicePdf------------Exception---------" + e);
    }
  }
 
  public void writeFiraPdfBulk(PrintOurBankVO printVO, List fileName)
  {
    logger.info("---------writeAdvicePdf---------------------");
    try
    {
      Document document = new Document(new Rectangle(PageSize.A4));
     
      File f = new File(ActionConstants.FILE_LOCATION + "FIRA" + "_" + printVO.getTransrefno() + ".pdf");
      if (f.exists()) {
        f.delete();
      }
      PdfWriter writer = PdfWriter.getInstance(document,
        new FileOutputStream(ActionConstants.FILE_LOCATION + "FIRA" + "_" + printVO.getTransrefno() + ".pdf"));
     
      ArrayList<PrintOurBankVO> billTypeList = null;
      ArrayList<PrintOurBankVO> accList = null;
      if (printVO != null)
      {
        billTypeList = printVO.getPrintList();
        accList = printVO.getAccList();
      }
      Font fontTitle = new Font(Font.FontFamily.COURIER, 12.0F, 1, BaseColor.BLACK);
      Font fontlet = new Font(Font.FontFamily.COURIER, 10.0F, 1, BaseColor.BLACK);
      Font fontList = new Font(Font.FontFamily.COURIER, 10.0F, 0, BaseColor.BLACK);
     
      Paragraph para = new Paragraph();
      Paragraph para1 = new Paragraph();
      Paragraph para2 = new Paragraph();
      Paragraph para3 = new Paragraph();
      Paragraph para4 = new Paragraph();
      Paragraph para5 = new Paragraph();
      Paragraph para6 = new Paragraph();
      Paragraph para7 = new Paragraph();
      Paragraph para8 = new Paragraph();
      Paragraph para9 = new Paragraph();
      Paragraph para10 = new Paragraph();
      Paragraph para11 = new Paragraph();
      Paragraph para12 = new Paragraph();
      Paragraph para13 = new Paragraph();
      Paragraph para14 = new Paragraph();
      Paragraph para15 = new Paragraph();
      Paragraph para16 = new Paragraph();
      Paragraph para17 = new Paragraph();
      Paragraph para18 = new Paragraph();
      Paragraph para19 = new Paragraph();
     
      para.setFont(fontList);
      para1.setFont(fontList);
      para2.setFont(fontList);
      para3.setFont(fontList);
      para4.setFont(fontList);
      para5.setFont(fontList);
      para6.setFont(fontList);
      para7.setFont(fontList);
      para8.setFont(fontList);
      para9.setFont(fontList);
      para10.setFont(fontList);
      para11.setFont(fontList);
      para12.setFont(fontList);
      para13.setFont(fontList);
      para14.setFont(fontList);
      para15.setFont(fontList);
      para16.setFont(fontList);
      para17.setFont(fontList);
      para18.setFont(fontList);
      para19.setFont(fontList);
      for (int i = 0; i < billTypeList.size(); i++)
      {
        PrintOurBankVO printOurBankVO = (PrintOurBankVO)billTypeList.get(i);
        if (printOurBankVO != null)
        {
          Chunk d1 = new Chunk("We certify that we have received the following remittance and proceeds thereof were paid to : ");
         
          para.setIndentationLeft(20.0F);
          para.setAlignment(0);
          para.add(d1);
         
          Chunk d2 = new Chunk("(a) To the beneficiary  ", fontlet);
          para1.setAlignment(0);
          para1.add(d2);
          para1.setTabSettings(new TabSettings(22.0F));
          para1.add(Chunk.TABBING);
          para1.add(new Chunk("  " + printOurBankVO.getBenificiarydetails()));
         
          Chunk d3 = new Chunk(
            "By Credit to Current/Saving/Cash Credit Account with us " + printVO.getAccNo());
          para2.setIndentationLeft(20.0F);
          para2.setAlignment(0);
          para2.add(d3);
         
          Chunk d11 = new Chunk("DATE OF CREDIT :  ");
          para18.setIndentationLeft(20.0F);
          para18.setAlignment(0);
          para18.add(d11);
          para18.setTabSettings(new TabSettings(22.0F));
          para18.add(Chunk.TABBING);
          para18.add(new Chunk("  " + printOurBankVO.getValue_date()));
         
          Chunk d4 = new Chunk("(b) To Bank on for credit of beneficiary's Account ", fontlet);
          para3.setAlignment(0);
          para3.add(d4);
         
          Chunk remName = new Chunk("Name and Place of Residence of Remitter ");
          para4.setIndentationLeft(20.0F);
          para4.setAlignment(0);
          para4.add(remName);
          para4.add(Chunk.TABBING);
          para4.add(new Chunk(
            ":   " + printOurBankVO.getOrderingcustomer() + " " + printOurBankVO.getRem_country()));
         
          Chunk remBank = new Chunk("Name and Place of Remitting bank ");
          para5.setIndentationLeft(20.0F);
          para5.setAlignment(0);
          para5.add(remBank);
          para5.setTabSettings(new TabSettings(126.0F));
          para5.add(Chunk.TABBING);
          para5.add(new Chunk(":   " + printOurBankVO.getRemmitngbankdetails()));
         
          Chunk mtNo = new Chunk("DD/TT/MT No ");
          para6.setIndentationLeft(20.0F);
          para6.setAlignment(0);
          para6.add(mtNo);
          para6.setTabSettings(new TabSettings(50.0F));
          para6.add(Chunk.TABBING);
          para6.add(new Chunk(
            ":   " + printOurBankVO.getNostroNo() + "  Dated  : " + printOurBankVO.getNostroDate()));
         
          Chunk foreignCurr = new Chunk("Foreign Currency Amount ", fontlet);
          para7.setIndentationLeft(20.0F);
          para7.setAlignment(0);
          para7.add(foreignCurr);
         
          int count = 0;
          double amount = 0.0D;
          String amountString = "";
          for (PrintOurBankVO printAccVal : accList)
          {
            amount += Double.parseDouble(printAccVal.getFircConAmt());
           
            amountString = getConvertedAmount(Double.valueOf(amount)).toString();
            if (!printAccVal.getFircEX().equals("1")) {
              count++;
            }
          }
          String equivalentStr = "";
          if (count > 0) {
            equivalentStr = " Equivalent to : INR " + getConvertedAmount(Double.valueOf(amount));
          }
          for (PrintOurBankVO printCurrList : accList)
          {
            logger.info("getFircCurrRec() :" + printCurrList.getFircCurrRec());
            logger.info("getFircCurr() :" + printCurrList.getFircCurr());
            if (printCurrList.getFircCurr() != null) {
              if (!printCurrList.getFircCurr().equals("INR"))
              {
                para7.setTabSettings(new TabSettings(20.0F));
                para7.add(Chunk.TABBING);
                para7.add(new Chunk(printOurBankVO.getCurrency() + "  " + printOurBankVO.getAmount() +
                  " and EEFC Amount " + printOurBankVO.getAmount() + " \n " + equivalentStr));
              }
              else
              {
                para7.setTabSettings(new TabSettings(20.0F));
                para7.add(Chunk.TABBING);
                para7.add(new Chunk(printOurBankVO.getCurrency() + "  " + printOurBankVO.getAmount() +
                  " and EEFC Amount 0.00" + " \n " + equivalentStr));
              }
            }
          }
          Chunk currencyDetails = new Chunk("Currency Conversion Details are as below :");
          para19.setIndentationLeft(20.0F);
          para19.setAlignment(0);
          para19.add(currencyDetails);
         
          PdfPTable table = new PdfPTable(5);
         
          table.setWidthPercentage(90.0F);
         
          PdfPCell cell1 = new PdfPCell(new Phrase("From Currency", fontList));
          cell1.setPadding(5.0F);
          cell1.setHorizontalAlignment(0);
          table.addCell(cell1);
         
          PdfPCell cell2 = new PdfPCell(new Phrase("Amount", fontList));
          cell2.setPadding(5.0F);
          cell2.setHorizontalAlignment(0);
          table.addCell(cell2);
         
          PdfPCell cell3 = new PdfPCell(new Phrase("Rate", fontList));
          cell3.setPadding(5.0F);
          cell3.setHorizontalAlignment(0);
          table.addCell(cell3);
         
          PdfPCell cell4 = new PdfPCell(new Phrase("To Currency", fontList));
          cell4.setPadding(5.0F);
          cell4.setHorizontalAlignment(0);
          table.addCell(cell4);
         
          PdfPCell cell5 = new PdfPCell(new Phrase("Amount", fontList));
          cell5.setPadding(5.0F);
          cell5.setHorizontalAlignment(0);
          table.addCell(cell5);
          for (int k = 0; k < accList.size(); k++)
          {
            PrintOurBankVO printAccVal = (PrintOurBankVO)accList.get(k);
            PdfPCell columnCell1 = new PdfPCell(new Phrase(printAccVal.getFircCurrRec(), fontList));
            columnCell1.setPadding(5.0F);
            columnCell1.setHorizontalAlignment(0);
            table.addCell(columnCell1);
           
            PdfPCell columnCell2 = new PdfPCell(new Phrase(printOurBankVO.getAmount(), fontList));
            columnCell2.setPadding(5.0F);
            columnCell2.setHorizontalAlignment(0);
            table.addCell(columnCell2);
           
            PdfPCell columnCell3 = new PdfPCell(new Phrase(printAccVal.getFircEX(), fontList));
            columnCell3.setPadding(5.0F);
            columnCell3.setHorizontalAlignment(0);
            table.addCell(columnCell3);
           
            PdfPCell columnCell4 = new PdfPCell(new Phrase(printAccVal.getFircCurr(), fontList));
            columnCell4.setPadding(5.0F);
            columnCell4.setHorizontalAlignment(0);
            table.addCell(columnCell4);
           
            PdfPCell columnCell5 = new PdfPCell(new Phrase(amountString, fontList));
            columnCell5.setPadding(5.0F);
            columnCell5.setHorizontalAlignment(0);
            table.addCell(columnCell5);
          }
          float[] colWidths = { 20.0F, 25.0F, 15.0F, 15.0F, 25.0F };
          table.setWidths(colWidths);
         
          Chunk purpose = new Chunk("Purpose of remittance as per Beneficiary / Remitter : ", fontlet);
          para11.setIndentationLeft(20.0F);
          para11.setAlignment(0);
          para11.add(purpose);
          para11.add(new Chunk(printOurBankVO.getPurposedesc()));
         
          Chunk payCertify = new Chunk(
            "We also certify that the payment thereof has / has not been received in non-convertible rupees or under any special trade or payments agreement.");
         
          para12.setIndentationLeft(20.0F);
          para12.setAlignment(0);
          para12.add(payCertify);
         
          Chunk appManner = new Chunk(
            "We confirm that we have obtained reimbursement in an approved manner.");
          para13.setIndentationLeft(20.0F);
          para13.setAlignment(0);
          para13.add(appManner);
         
          Chunk certificate = new Chunk("Purpose if advance export then the shipment of goods should be made within one year from date of receipt of advance payment.");
         
          para14.setIndentationLeft(20.0F);
          para14.setAlignment(0);
          para14.add(certificate);
         
          Chunk signature = new Chunk("This is system generated advice and no signature is required.");
          para15.setIndentationLeft(20.0F);
          para15.setAlignment(0);
          para15.add(signature);
         
          Chunk ourBank = new Chunk("KOTAK MAHINDRA BANK LTD\n" + printOurBankVO.getAddress1() + "\n" +
            printOurBankVO.getAddress2() + "\n" + printOurBankVO.getAddress3() + "\n" +
            printOurBankVO.getAddress4() + "\n" + printOurBankVO.getAddress5(), fontlet);
          para16.setIndentationLeft(20.0F);
          para16.setAlignment(0);
          para16.add(ourBank);
         
          Chunk linebreak = new Chunk(new DottedLineSeparator());
         
          document.open();
         
          float[] columnWidths1 = { 10.0F };
         
          PdfPTable table1 = new PdfPTable(columnWidths1);
          table1.setWidthPercentage(100.0F);
          table1.getDefaultCell().setBorder(0);
          table1.getDefaultCell().setHorizontalAlignment(1);
          table1.addCell(para16);
          document.add(table1);
         
          document.add(linebreak);
         
          float[] columnWidths = { 10.0F, 10.0F };
          PdfPTable table2 = new PdfPTable(columnWidths);
          table2.setWidthPercentage(100.0F);
          table2.getDefaultCell().setBorder(0);
          table2.getDefaultCell().setHorizontalAlignment(0);
          Phrase firstLineLeft = new Phrase("Ref No : " + printOurBankVO.getTransrefno(), fontTitle);
          table2.addCell(firstLineLeft);
          table2.getDefaultCell().setHorizontalAlignment(2);
          Phrase firstLineRight = new Phrase("Date : " + printOurBankVO.getSystemDate(), fontTitle);
          table2.addCell(firstLineRight);
         
          document.add(table2);
          document.add(Chunk.NEWLINE);
         
          Chunk title = new Chunk("FOREIGN INWARD REMITTANCE ADVICE", fontlet);
          para17.setIndentationLeft(20.0F);
          para17.setAlignment(0);
          para17.add(title);
         
          PdfPTable table3 = new PdfPTable(columnWidths1);
          table3.setWidthPercentage(100.0F);
          table3.getDefaultCell().setBorder(0);
          table3.getDefaultCell().setHorizontalAlignment(1);
          table3.addCell(para17);
          document.add(table3);
         
          document.add(para);
         
          document.add(para1);
          document.add(para2);
         
          document.add(para18);
         
          document.add(Chunk.NEWLINE);
          document.add(para3);
          document.add(para4);
          document.add(para5);
          document.add(para6);
          document.add(para7);
         
          document.add(para19);
          document.add(Chunk.NEWLINE);
          document.add(table);
         
          document.add(Chunk.NEWLINE);
          document.add(para11);
          document.add(Chunk.NEWLINE);
          document.add(para12);
          document.add(Chunk.NEWLINE);
          document.add(para13);
          document.add(Chunk.NEWLINE);
          document.add(para14);
          document.add(Chunk.NEWLINE);
          document.add(para15);
          document.close();
        }
      }
      writer.close();
      logger.info("Test---------------1234567----");
    }
    catch (Exception e)
    {
      logger.info("---------writeAdvicePdf------------Exception---------" + e);
    }
  }
 
  public void writeAdvicePdfBulk(PrintOurBankVO printVO, List fileName)
    throws Exception
  {
    logger.info("---------writeAdvicePdf---------------------");
    try
    {
      Document document = new Document(new Rectangle(PageSize.A4));
     
      File f = new File(ActionConstants.FILE_LOCATION + "FIRA" + printVO.getTransrefno() + ".pdf");
      if (f.exists()) {
        f.delete();
      }
      PdfWriter writer = PdfWriter.getInstance(document,
        new FileOutputStream(ActionConstants.FILE_LOCATION + "FIRA" + printVO.getTransrefno() + ".pdf"));
     
      ArrayList<PrintOurBankVO> billTypeList = null;
      ArrayList<PrintOurBankVO> accList = null;
      if (printVO != null)
      {
        billTypeList = printVO.getPrintList();
        accList = printVO.getAccList();
      }
      Font fontTitle = new Font(Font.FontFamily.COURIER, 12.0F, 1, BaseColor.BLACK);
      Font fontlet = new Font(Font.FontFamily.COURIER, 10.0F, 1, BaseColor.BLACK);
      Font fontList = new Font(Font.FontFamily.COURIER, 10.0F, 0, BaseColor.BLACK);
     
      Paragraph para = new Paragraph();
      Paragraph para1 = new Paragraph();
      Paragraph para2 = new Paragraph();
      Paragraph para3 = new Paragraph();
      Paragraph para4 = new Paragraph();
      Paragraph para5 = new Paragraph();
      Paragraph para6 = new Paragraph();
      Paragraph para7 = new Paragraph();
      Paragraph para8 = new Paragraph();
      Paragraph para9 = new Paragraph();
      Paragraph para10 = new Paragraph();
      Paragraph para11 = new Paragraph();
      Paragraph para12 = new Paragraph();
      Paragraph para13 = new Paragraph();
      Paragraph para14 = new Paragraph();
      Paragraph para15 = new Paragraph();
      Paragraph para16 = new Paragraph();
      Paragraph para17 = new Paragraph();
      Paragraph para18 = new Paragraph();
      Paragraph para19 = new Paragraph();
     
      para.setFont(fontList);
      para1.setFont(fontList);
      para2.setFont(fontList);
      para3.setFont(fontList);
      para4.setFont(fontList);
      para5.setFont(fontList);
      para6.setFont(fontList);
      para7.setFont(fontList);
      para8.setFont(fontList);
      para9.setFont(fontList);
      para10.setFont(fontList);
      para11.setFont(fontList);
      para12.setFont(fontList);
      para13.setFont(fontList);
      para14.setFont(fontList);
      para15.setFont(fontList);
      para16.setFont(fontList);
      para17.setFont(fontList);
      para18.setFont(fontList);
      para19.setFont(fontList);
      for (int i = 0; i < billTypeList.size(); i++)
      {
        PrintOurBankVO printOurBankVO = (PrintOurBankVO)billTypeList.get(i);
        if (printOurBankVO != null)
        {
          Chunk d1 = new Chunk("We certify that we have received the following remittance and proceeds thereof were paid to : ");
         
          para.setIndentationLeft(20.0F);
          para.setAlignment(0);
          para.add(d1);
         
          Chunk d2 = new Chunk("(a) To the beneficiary  ", fontlet);
          para1.setAlignment(0);
          para1.add(d2);
          para1.setTabSettings(new TabSettings(22.0F));
          para1.add(Chunk.TABBING);
          para1.add(new Chunk("  " + printOurBankVO.getBenificiarydetails()));
         
          Chunk d3 = new Chunk(
            "By Credit to Current/Saving/Cash Credit Account with us " + printVO.getAccNo());
          para2.setIndentationLeft(20.0F);
          para2.setAlignment(0);
          para2.add(d3);
         
          Chunk d11 = new Chunk("DATE OF CREDIT :  ");
          para18.setIndentationLeft(20.0F);
          para18.setAlignment(0);
          para18.add(d11);
          para18.setTabSettings(new TabSettings(22.0F));
          para18.add(Chunk.TABBING);
          para18.add(new Chunk("  " + printOurBankVO.getValue_date()));
         
          Chunk d4 = new Chunk("(b) To Bank on for credit of beneficiary's Account ", fontlet);
          para3.setAlignment(0);
          para3.add(d4);
         
          Chunk remName = new Chunk("Name and Place of Residence of Remitter ");
          para4.setIndentationLeft(20.0F);
          para4.setAlignment(0);
          para4.add(remName);
          para4.add(Chunk.TABBING);
          para4.add(new Chunk(
            ":   " + printOurBankVO.getOrderingcustomer() + " " + printOurBankVO.getRem_country()));
         
          Chunk remBank = new Chunk("Name and Place of Remitting bank ");
          para5.setIndentationLeft(20.0F);
          para5.setAlignment(0);
          para5.add(remBank);
          para5.setTabSettings(new TabSettings(126.0F));
          para5.add(Chunk.TABBING);
          para5.add(new Chunk(":   " + printOurBankVO.getRemmitngbankdetails()));
         
          Chunk mtNo = new Chunk("DD/TT/MT No ");
          para6.setIndentationLeft(20.0F);
          para6.setAlignment(0);
          para6.add(mtNo);
          para6.setTabSettings(new TabSettings(50.0F));
          para6.add(Chunk.TABBING);
          para6.add(new Chunk(
            ":   " + printOurBankVO.getNostroNo() + "  Dated  : " + printOurBankVO.getNostroDate()));
         
          Chunk foreignCurr = new Chunk("Foreign Currency Amount ", fontlet);
          para7.setIndentationLeft(20.0F);
          para7.setAlignment(0);
          para7.add(foreignCurr);
         
          int count = 0;
          double amount = 0.0D;
          String amountString = "";
          for (PrintOurBankVO printAccVal : accList)
          {
            amount += Double.parseDouble(printAccVal.getFircConAmt());
           
            amountString = getConvertedAmount(Double.valueOf(amount)).toString();
            if (!printAccVal.getFircEX().equals("1")) {
              count++;
            }
          }
          String equivalentStr = "";
          if (count > 0) {
            equivalentStr = " Equivalent to : INR " + getConvertedAmount(Double.valueOf(amount));
          }
          for (PrintOurBankVO printCurrList : accList)
          {
            logger.info("getFircCurrRec() :" + printCurrList.getFircCurrRec());
            logger.info("getFircCurr() :" + printCurrList.getFircCurr());
            if (printCurrList.getFircCurr() != null) {
              if (!printCurrList.getFircCurr().equals("INR"))
              {
                para7.setTabSettings(new TabSettings(20.0F));
                para7.add(Chunk.TABBING);
                para7.add(new Chunk(printOurBankVO.getCurrency() + "  " + printOurBankVO.getAmount() +
                  " and EEFC Amount " + printOurBankVO.getAmount() + " \n " + equivalentStr));
              }
              else
              {
                para7.setTabSettings(new TabSettings(20.0F));
                para7.add(Chunk.TABBING);
                para7.add(new Chunk(printOurBankVO.getCurrency() + "  " + printOurBankVO.getAmount() +
                  " and EEFC Amount 0.00" + " \n " + equivalentStr));
              }
            }
          }
          Chunk currencyDetails = new Chunk("Currency Conversion Details are as below :");
          para19.setIndentationLeft(20.0F);
          para19.setAlignment(0);
          para19.add(currencyDetails);
         
          PdfPTable table = new PdfPTable(5);
         
          table.setWidthPercentage(90.0F);
         
          PdfPCell cell1 = new PdfPCell(new Phrase("From Currency", fontList));
          cell1.setPadding(5.0F);
          cell1.setHorizontalAlignment(0);
          table.addCell(cell1);
         
          PdfPCell cell2 = new PdfPCell(new Phrase("Amount", fontList));
          cell2.setPadding(5.0F);
          cell2.setHorizontalAlignment(0);
          table.addCell(cell2);
         
          PdfPCell cell3 = new PdfPCell(new Phrase("Rate", fontList));
          cell3.setPadding(5.0F);
          cell3.setHorizontalAlignment(0);
          table.addCell(cell3);
         
          PdfPCell cell4 = new PdfPCell(new Phrase("To Currency", fontList));
          cell4.setPadding(5.0F);
          cell4.setHorizontalAlignment(0);
          table.addCell(cell4);
         
          PdfPCell cell5 = new PdfPCell(new Phrase("Amount", fontList));
          cell5.setPadding(5.0F);
          cell5.setHorizontalAlignment(0);
          table.addCell(cell5);
          for (int k = 0; k < accList.size(); k++)
          {
            PrintOurBankVO printAccVal = (PrintOurBankVO)accList.get(k);
            PdfPCell columnCell1 = new PdfPCell(new Phrase(printAccVal.getFircCurrRec(), fontList));
            columnCell1.setPadding(5.0F);
            columnCell1.setHorizontalAlignment(0);
            table.addCell(columnCell1);
           
            PdfPCell columnCell2 = new PdfPCell(new Phrase(printOurBankVO.getAmount(), fontList));
            columnCell2.setPadding(5.0F);
            columnCell2.setHorizontalAlignment(0);
            table.addCell(columnCell2);
           
            PdfPCell columnCell3 = new PdfPCell(new Phrase(printAccVal.getFircEX(), fontList));
            columnCell3.setPadding(5.0F);
            columnCell3.setHorizontalAlignment(0);
            table.addCell(columnCell3);
           
            PdfPCell columnCell4 = new PdfPCell(new Phrase(printAccVal.getFircCurr(), fontList));
            columnCell4.setPadding(5.0F);
            columnCell4.setHorizontalAlignment(0);
            table.addCell(columnCell4);
           
            PdfPCell columnCell5 = new PdfPCell(new Phrase(amountString, fontList));
            columnCell5.setPadding(5.0F);
            columnCell5.setHorizontalAlignment(0);
            table.addCell(columnCell5);
          }
          float[] colWidths = { 20.0F, 25.0F, 15.0F, 15.0F, 25.0F };
          table.setWidths(colWidths);
         
          Chunk purpose = new Chunk("Purpose of remittance as per Beneficiary / Remitter : ", fontlet);
          para11.setIndentationLeft(20.0F);
          para11.setAlignment(0);
          para11.add(purpose);
          para11.add(new Chunk(printOurBankVO.getPurposedesc()));
         
          Chunk payCertify = new Chunk(
            "We also certify that the payment thereof has / has not been received in non-convertible rupees or under any special trade or payments agreement.");
         
          para12.setIndentationLeft(20.0F);
          para12.setAlignment(0);
          para12.add(payCertify);
         
          Chunk appManner = new Chunk(
            "We confirm that we have obtained reimbursement in an approved manner.");
          para13.setIndentationLeft(20.0F);
          para13.setAlignment(0);
          para13.add(appManner);
         
          Chunk certificate = new Chunk("Purpose if advance export then the shipment of goods should be made within one year from date of receipt of advance payment.");
         
          para14.setIndentationLeft(20.0F);
          para14.setAlignment(0);
          para14.add(certificate);
         
          Chunk signature = new Chunk("This is system generated advice and no signature is required.");
          para15.setIndentationLeft(20.0F);
          para15.setAlignment(0);
          para15.add(signature);
         
          Chunk ourBank = new Chunk("KOTAK MAHINDRA BANK LTD\n" + printOurBankVO.getAddress1() + "\n" +
            printOurBankVO.getAddress2() + "\n" + printOurBankVO.getAddress3() + "\n" +
            printOurBankVO.getAddress4() + "\n" + printOurBankVO.getAddress5(), fontlet);
          para16.setIndentationLeft(20.0F);
          para16.setAlignment(0);
          para16.add(ourBank);
         
          Chunk linebreak = new Chunk(new DottedLineSeparator());
         
          document.open();
         
          float[] columnWidths1 = { 10.0F };
         
          PdfPTable table1 = new PdfPTable(columnWidths1);
          table1.setWidthPercentage(100.0F);
          table1.getDefaultCell().setBorder(0);
          table1.getDefaultCell().setHorizontalAlignment(1);
          table1.addCell(para16);
          document.add(table1);
         
          document.add(linebreak);
         
          float[] columnWidths = { 10.0F, 10.0F };
          PdfPTable table2 = new PdfPTable(columnWidths);
          table2.setWidthPercentage(100.0F);
          table2.getDefaultCell().setBorder(0);
          table2.getDefaultCell().setHorizontalAlignment(0);
          Phrase firstLineLeft = new Phrase("Ref No : " + printOurBankVO.getTransrefno(), fontTitle);
          table2.addCell(firstLineLeft);
          table2.getDefaultCell().setHorizontalAlignment(2);
          Phrase firstLineRight = new Phrase("Date : " + printOurBankVO.getSystemDate(), fontTitle);
          table2.addCell(firstLineRight);
         
          document.add(table2);
          document.add(Chunk.NEWLINE);
         
          Chunk title = new Chunk("FOREIGN INWARD REMITTANCE ADVICE", fontlet);
          para17.setIndentationLeft(20.0F);
          para17.setAlignment(0);
          para17.add(title);
         
          PdfPTable table3 = new PdfPTable(columnWidths1);
          table3.setWidthPercentage(100.0F);
          table3.getDefaultCell().setBorder(0);
          table3.getDefaultCell().setHorizontalAlignment(1);
          table3.addCell(para17);
          document.add(table3);
         
          document.add(para);
         
          document.add(para1);
          document.add(para2);
         
          document.add(para18);
         
          document.add(Chunk.NEWLINE);
          document.add(para3);
          document.add(para4);
          document.add(para5);
          document.add(para6);
          document.add(para7);
         
          document.add(para19);
          document.add(Chunk.NEWLINE);
          document.add(table);
         
          document.add(Chunk.NEWLINE);
          document.add(para11);
          document.add(Chunk.NEWLINE);
          document.add(para12);
          document.add(Chunk.NEWLINE);
          document.add(para13);
          document.add(Chunk.NEWLINE);
          document.add(para14);
          document.add(Chunk.NEWLINE);
          document.add(para15);
          document.close();
        }
      }
      writer.close();
      logger.info("Test---------------1234567----");
    }
    catch (Exception e)
    {
      logger.info("---------writeAdvicePdf------------Exception---------" + e);
    }
  }
 
  public void writeFiraPdf(PrintOurBankVO printVO, ByteArrayOutputStream baos)
  {
    logger.info("---------writeFiraPdf---------------------");
    try
    {
      Document document = new Document(new Rectangle(PageSize.A4));
     
      PdfWriter writer = PdfWriter.getInstance(document, baos);
      ArrayList<PrintOurBankVO> billTypeList = null;
      ArrayList<PrintOurBankVO> accList = null;
      if (printVO != null)
      {
        billTypeList = printVO.getPrintList();
        accList = printVO.getAccList();
      }
      Font fontTitle = new Font(Font.FontFamily.COURIER, 12.0F, 1, BaseColor.BLACK);
      Font fontlet = new Font(Font.FontFamily.COURIER, 10.0F, 1, BaseColor.BLACK);
      Font fontList = new Font(Font.FontFamily.COURIER, 10.0F, 0, BaseColor.BLACK);
     
      Paragraph para = new Paragraph();
      Paragraph para1 = new Paragraph();
      Paragraph para2 = new Paragraph();
      Paragraph para3 = new Paragraph();
      Paragraph para4 = new Paragraph();
      Paragraph para5 = new Paragraph();
      Paragraph para6 = new Paragraph();
      Paragraph para7 = new Paragraph();
      Paragraph para8 = new Paragraph();
      Paragraph para9 = new Paragraph();
      Paragraph para10 = new Paragraph();
      Paragraph para11 = new Paragraph();
      Paragraph para12 = new Paragraph();
      Paragraph para13 = new Paragraph();
      Paragraph para14 = new Paragraph();
      Paragraph para15 = new Paragraph();
      Paragraph para16 = new Paragraph();
      Paragraph para17 = new Paragraph();
      Paragraph para18 = new Paragraph();
      Paragraph para19 = new Paragraph();
     
      para.setFont(fontList);
      para1.setFont(fontList);
      para2.setFont(fontList);
      para3.setFont(fontList);
      para4.setFont(fontList);
      para5.setFont(fontList);
      para6.setFont(fontList);
      para7.setFont(fontList);
      para8.setFont(fontList);
      para9.setFont(fontList);
      para10.setFont(fontList);
      para11.setFont(fontList);
      para12.setFont(fontList);
      para13.setFont(fontList);
      para14.setFont(fontList);
      para15.setFont(fontList);
      para16.setFont(fontList);
      para17.setFont(fontList);
      para18.setFont(fontList);
      para19.setFont(fontList);
      for (int i = 0; i < billTypeList.size(); i++)
      {
        PrintOurBankVO printOurBankVO = (PrintOurBankVO)billTypeList.get(i);
        if (printOurBankVO != null)
        {
          Chunk d1 = new Chunk("We certify that we have received the following remittance and proceeds thereof were paid to : ");
         
          para.setIndentationLeft(20.0F);
          para.setAlignment(0);
          para.add(d1);
         
          Chunk d2 = new Chunk("(a) To the beneficiary  ", fontlet);
          para1.setAlignment(0);
          para1.add(d2);
          para1.setTabSettings(new TabSettings(22.0F));
          para1.add(Chunk.TABBING);
          para1.add(new Chunk("  " + printOurBankVO.getBenificiarydetails()));
         
          Chunk d3 = new Chunk(
            "By Credit to Current/Saving/Cash Credit Account with us " + printVO.getAccNo());
          para2.setIndentationLeft(20.0F);
          para2.setAlignment(0);
          para2.add(d3);
         
          Chunk d11 = new Chunk("DATE OF CREDIT :  ");
          para18.setIndentationLeft(20.0F);
          para18.setAlignment(0);
          para18.add(d11);
          para18.setTabSettings(new TabSettings(22.0F));
          para18.add(Chunk.TABBING);
          para18.add(new Chunk("  " + printOurBankVO.getValue_date()));
         
          Chunk d4 = new Chunk("(b) To Bank on for credit of beneficiary's Account ", fontlet);
          para3.setAlignment(0);
          para3.add(d4);
         
          Chunk remName = new Chunk("Name and Place of Residence of Remitter ");
          para4.setIndentationLeft(20.0F);
          para4.setAlignment(0);
          para4.add(remName);
          para4.add(Chunk.TABBING);
          para4.add(new Chunk(
            ":   " + printOurBankVO.getOrderingcustomer() + " " + printOurBankVO.getRem_country()));
         
          Chunk remBank = new Chunk("Name and Place of Remitting bank ");
          para5.setIndentationLeft(20.0F);
          para5.setAlignment(0);
          para5.add(remBank);
          para5.setTabSettings(new TabSettings(126.0F));
          para5.add(Chunk.TABBING);
          para5.add(new Chunk(":   " + printOurBankVO.getRemmitngbankdetails()));
         
          Chunk mtNo = new Chunk("DD/TT/MT No ");
          para6.setIndentationLeft(20.0F);
          para6.setAlignment(0);
          para6.add(mtNo);
          para6.setTabSettings(new TabSettings(50.0F));
          para6.add(Chunk.TABBING);
          para6.add(new Chunk(
            ":   " + printOurBankVO.getNostroNo() + "  Dated  : " + printOurBankVO.getNostroDate()));
         
          Chunk foreignCurr = new Chunk("Foreign Currency Amount ", fontlet);
          para7.setIndentationLeft(20.0F);
          para7.setAlignment(0);
          para7.add(foreignCurr);
         
          int count = 0;
          double amount = 0.0D;
          String amountString = "";
          for (PrintOurBankVO printAccVal : accList)
          {
            amount += Double.parseDouble(printAccVal.getFircConAmt());
           
            amountString = getConvertedAmount(Double.valueOf(amount)).toString();
            if (!printAccVal.getFircEX().equals("1")) {
              count++;
            }
          }
          String equivalentStr = "";
          if (count > 0) {
            equivalentStr = " Equivalent to : INR " + getConvertedAmount(Double.valueOf(amount));
          }
          for (PrintOurBankVO printCurrList : accList)
          {
            logger.info("getFircCurrRec() :" + printCurrList.getFircCurrRec());
            logger.info("getFircCurr() :" + printCurrList.getFircCurr());
            if (printCurrList.getFircCurr() != null) {
              if (!printCurrList.getFircCurr().equals("INR"))
              {
                para7.setTabSettings(new TabSettings(20.0F));
                para7.add(Chunk.TABBING);
                para7.add(new Chunk(printOurBankVO.getCurrency() + "  " + printOurBankVO.getAmount() +
                  " and EEFC Amount " + printOurBankVO.getAmount() + " \n " + equivalentStr));
              }
              else
              {
                para7.setTabSettings(new TabSettings(20.0F));
                para7.add(Chunk.TABBING);
                para7.add(new Chunk(printOurBankVO.getCurrency() + "  " + printOurBankVO.getAmount() +
                  " and EEFC Amount 0.00" + " \n " + equivalentStr));
              }
            }
          }
          Chunk currencyDetails = new Chunk("Currency Conversion Details are as below :");
          para19.setIndentationLeft(20.0F);
          para19.setAlignment(0);
          para19.add(currencyDetails);
         
          PdfPTable table = new PdfPTable(5);
         
          table.setWidthPercentage(90.0F);
         
          PdfPCell cell1 = new PdfPCell(new Phrase("From Currency", fontList));
          cell1.setPadding(5.0F);
          cell1.setHorizontalAlignment(0);
          table.addCell(cell1);
         
          PdfPCell cell2 = new PdfPCell(new Phrase("Amount", fontList));
          cell2.setPadding(5.0F);
          cell2.setHorizontalAlignment(0);
          table.addCell(cell2);
         
          PdfPCell cell3 = new PdfPCell(new Phrase("Rate", fontList));
          cell3.setPadding(5.0F);
          cell3.setHorizontalAlignment(0);
          table.addCell(cell3);
         
          PdfPCell cell4 = new PdfPCell(new Phrase("To Currency", fontList));
          cell4.setPadding(5.0F);
          cell4.setHorizontalAlignment(0);
          table.addCell(cell4);
         
          PdfPCell cell5 = new PdfPCell(new Phrase("Amount", fontList));
          cell5.setPadding(5.0F);
          cell5.setHorizontalAlignment(0);
          table.addCell(cell5);
          for (int k = 0; k < accList.size(); k++)
          {
            PrintOurBankVO printAccVal = (PrintOurBankVO)accList.get(k);
            PdfPCell columnCell1 = new PdfPCell(new Phrase(printAccVal.getFircCurrRec(), fontList));
            columnCell1.setPadding(5.0F);
            columnCell1.setHorizontalAlignment(0);
            table.addCell(columnCell1);
           
            PdfPCell columnCell2 = new PdfPCell(new Phrase(printOurBankVO.getAmount(), fontList));
            columnCell2.setPadding(5.0F);
            columnCell2.setHorizontalAlignment(0);
            table.addCell(columnCell2);
           
            PdfPCell columnCell3 = new PdfPCell(new Phrase(printAccVal.getFircEX(), fontList));
            columnCell3.setPadding(5.0F);
            columnCell3.setHorizontalAlignment(0);
            table.addCell(columnCell3);
           
            PdfPCell columnCell4 = new PdfPCell(new Phrase(printAccVal.getFircCurr(), fontList));
            columnCell4.setPadding(5.0F);
            columnCell4.setHorizontalAlignment(0);
            table.addCell(columnCell4);
           
            PdfPCell columnCell5 = new PdfPCell(new Phrase(amountString, fontList));
            columnCell5.setPadding(5.0F);
            columnCell5.setHorizontalAlignment(0);
            table.addCell(columnCell5);
          }
          float[] colWidths = { 20.0F, 25.0F, 15.0F, 15.0F, 25.0F };
          table.setWidths(colWidths);
         
          Chunk purpose = new Chunk("Purpose of remittance as per Beneficiary / Remitter : ", fontlet);
          para11.setIndentationLeft(20.0F);
          para11.setAlignment(0);
          para11.add(purpose);
          para11.add(new Chunk(printOurBankVO.getPurposedesc()));
         
          Chunk payCertify = new Chunk(
            "We also certify that the payment thereof has / has not been received in non-convertible rupees or under any special trade or payments agreement.");
         
          para12.setIndentationLeft(20.0F);
          para12.setAlignment(0);
          para12.add(payCertify);
         
          Chunk appManner = new Chunk(
            "We confirm that we have obtained reimbursement in an approved manner.");
          para13.setIndentationLeft(20.0F);
          para13.setAlignment(0);
          para13.add(appManner);
         
          Chunk certificate = new Chunk("Purpose if advance export then the shipment of goods should be made within one year from date of receipt of advance payment.");
         
          para14.setIndentationLeft(20.0F);
          para14.setAlignment(0);
          para14.add(certificate);
         
          Chunk signature = new Chunk("This is system generated advice and no signature is required.");
          para15.setIndentationLeft(20.0F);
          para15.setAlignment(0);
          para15.add(signature);
         

          Chunk ourBank = new Chunk("KOTAK MAHINDRA BANK LTD\n" + printOurBankVO.getAddress1() + "\n" +
            printOurBankVO.getAddress2() + "\n" + printOurBankVO.getAddress3() + "\n" +
            printOurBankVO.getAddress4() + "\n" + printOurBankVO.getAddress5(), fontlet);
          para16.setIndentationLeft(20.0F);
          para16.setAlignment(0);
          para16.add(ourBank);
         
          Chunk linebreak = new Chunk(new DottedLineSeparator());
         
          document.open();
         
          float[] columnWidths1 = { 10.0F };
         
          PdfPTable table1 = new PdfPTable(columnWidths1);
          table1.setWidthPercentage(100.0F);
          table1.getDefaultCell().setBorder(0);
          table1.getDefaultCell().setHorizontalAlignment(1);
          table1.addCell(para16);
          document.add(table1);
         
          document.add(linebreak);
         
          float[] columnWidths = { 10.0F, 10.0F };
          PdfPTable table2 = new PdfPTable(columnWidths);
          table2.setWidthPercentage(100.0F);
          table2.getDefaultCell().setBorder(0);
          table2.getDefaultCell().setHorizontalAlignment(0);
          Phrase firstLineLeft = new Phrase("Ref No : " + printOurBankVO.getTransrefno(), fontTitle);
          table2.addCell(firstLineLeft);
          table2.getDefaultCell().setHorizontalAlignment(2);
          Phrase firstLineRight = new Phrase("Date : " + printOurBankVO.getSystemDate(), fontTitle);
          table2.addCell(firstLineRight);
         
          document.add(table2);
          document.add(Chunk.NEWLINE);
         
          Chunk title = new Chunk("FOREIGN INWARD REMITTANCE ADVICE", fontlet);
          para17.setIndentationLeft(20.0F);
          para17.setAlignment(0);
          para17.add(title);
         
          PdfPTable table3 = new PdfPTable(columnWidths1);
          table3.setWidthPercentage(100.0F);
          table3.getDefaultCell().setBorder(0);
          table3.getDefaultCell().setHorizontalAlignment(1);
          table3.addCell(para17);
          document.add(table3);
         
          document.add(para);
         
          document.add(para1);
          document.add(para2);
         
          document.add(para18);
         
          document.add(Chunk.NEWLINE);
          document.add(para3);
          document.add(para4);
          document.add(para5);
          document.add(para6);
          document.add(para7);
         
          document.add(para19);
          document.add(Chunk.NEWLINE);
          document.add(table);
         
          document.add(Chunk.NEWLINE);
          document.add(para11);
          document.add(Chunk.NEWLINE);
          document.add(para12);
          document.add(Chunk.NEWLINE);
          document.add(para13);
          document.add(Chunk.NEWLINE);
          document.add(para14);
          document.add(Chunk.NEWLINE);
          document.add(para15);
          document.close();
        }
      }
      writer.close();
      logger.info("Test---------------writeFiraPdf done----");
    }
    catch (Exception e)
    {
      logger.info("---------writeFiraPdf------------Exception---------" + e);
    }
  }
 
  public static BigDecimal getConvertedAmount(Double amount)
  {
    BigDecimal convertedAmount = new BigDecimal(amount.doubleValue()).setScale(1, RoundingMode.HALF_EVEN);
    System.out.println("Converting amount-->" + convertedAmount);
    return convertedAmount;
  }
}