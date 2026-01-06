package in.co.FIRC.utility;

import freemarker.log.Logger;
import in.co.FIRC.vo.ourBank.PrintOurBankVO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc.Enum;

public class WordDocGenerator
{
  private static Logger logger = Logger.getLogger(WordDocGenerator.class.getName());
 
  public void writeDoc(PrintOurBankVO printVO, ByteArrayOutputStream baos, String type, boolean duplicateCopy, boolean emailNotifSend, String fileName)
  {
    String beneficiary = null;
    String orderingCustomer = null;
    String remittingBank = null;
    String purposeDesc = null;
    int benAddress = 0;int benAddrLen = 0;
    int ordCustAddress = 0;int ordCustAddrLen = 0;
    int remAddress = 0;int remBankLen = 0;
   
    int purpDesc = 0;
    CommonMethods com = new CommonMethods();
    try
    {
      beneficiary = ((PrintOurBankVO)printVO.getPrintList().get(0)).getBenificiarydetails();
      if ((beneficiary != null) && (beneficiary.trim().length() > 0)) {
        benAddrLen = beneficiary.length();
      }
      if (benAddrLen > 48) {
        benAddress = 2;
      } else {
        benAddress = 1;
      }
      if (benAddrLen > 96) {
        benAddress = 3;
      }
      logger.info("Beneficiary address: " + beneficiary);
     

      XWPFDocument document = new XWPFDocument();
      if (emailNotifSend)
      {
        File f = new File(ActionConstants.FILE_LOCATION + fileName + ".docx");
        if (f.exists()) {
          f.delete();
        }
        FileInputStream fis = new FileInputStream(f.getAbsolutePath());
        document = new XWPFDocument(OPCPackage.open(fis));
      }
      CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
      CTPageMar pageMar = sectPr.addNewPgMar();
      pageMar.setLeft(BigInteger.valueOf(720L));
      pageMar.setTop(BigInteger.valueOf(360L));
      pageMar.setRight(BigInteger.valueOf(720L));
      pageMar.setBottom(BigInteger.valueOf(130L));
     

      XWPFParagraph top1 = document.createParagraph();
      top1.setAlignment(ParagraphAlignment.CENTER);
      XWPFRun run_top1 = top1.createRun();
      run_top1.setFontFamily("Courier New");
      run_top1.setFontSize(10);
      run_top1.setBold(true);
      run_top1.setText("KOTAK MAHINDRA BANK LTD");
      setLineSpacing(top1, 0);
      if ((!CommonMethods.isNull(((PrintOurBankVO)printVO.getPrintList().get(0)).getAddress1())) &&
        (((PrintOurBankVO)printVO.getPrintList().get(0)).getAddress1().trim().length() > 0))
      {
        XWPFParagraph top2 = document.createParagraph();
        top2.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run_top2 = top2.createRun();
        run_top2.setFontFamily("Courier New");
        run_top2.setFontSize(10);
        run_top2.setBold(true);
        run_top2.setText(((PrintOurBankVO)printVO.getPrintList().get(0)).getAddress1());
        setLineSpacing(top2, 0);
      }
      if ((!CommonMethods.isNull(((PrintOurBankVO)printVO.getPrintList().get(0)).getAddress2())) &&
        (((PrintOurBankVO)printVO.getPrintList().get(0)).getAddress2().trim().length() > 0))
      {
        XWPFParagraph top3 = document.createParagraph();
        top3.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run_top3 = top3.createRun();
        run_top3.setFontFamily("Courier New");
        run_top3.setFontSize(10);
        run_top3.setBold(true);
        run_top3.setText(((PrintOurBankVO)printVO.getPrintList().get(0)).getAddress2());
        setLineSpacing(top3, 0);
      }
      if ((!CommonMethods.isNull(((PrintOurBankVO)printVO.getPrintList().get(0)).getAddress3())) &&
        (((PrintOurBankVO)printVO.getPrintList().get(0)).getAddress3().trim().length() > 0))
      {
        XWPFParagraph top4 = document.createParagraph();
        top4.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run_top4 = top4.createRun();
        run_top4.setFontFamily("Courier New");
        run_top4.setFontSize(10);
        run_top4.setBold(true);
        run_top4.setText(((PrintOurBankVO)printVO.getPrintList().get(0)).getAddress3());
        setLineSpacing(top4, 0);
      }
      if ((!CommonMethods.isNull(((PrintOurBankVO)printVO.getPrintList().get(0)).getAddress4())) &&
        (((PrintOurBankVO)printVO.getPrintList().get(0)).getAddress4().trim().length() > 0))
      {
        XWPFParagraph top5 = document.createParagraph();
        top5.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run_top5 = top5.createRun();
        run_top5.setFontFamily("Courier New");
        run_top5.setFontSize(10);
        run_top5.setBold(true);
        run_top5.setText(((PrintOurBankVO)printVO.getPrintList().get(0)).getAddress4());
        setLineSpacing(top5, 0);
      }
      if ((!CommonMethods.isNull(((PrintOurBankVO)printVO.getPrintList().get(0)).getAddress5())) &&
        (((PrintOurBankVO)printVO.getPrintList().get(0)).getAddress5().trim().length() > 0))
      {
        XWPFParagraph top6 = document.createParagraph();
        top6.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run_top6 = top6.createRun();
        run_top6.setFontFamily("Courier New");
        run_top6.setFontSize(10);
        run_top6.setBold(true);
        run_top6.setText(((PrintOurBankVO)printVO.getPrintList().get(0)).getAddress5());
        setLineSpacing(top6, 0);
      }
      XWPFParagraph line = document.createParagraph();
      XWPFRun run = line.createRun();
      run.setFontFamily("Courier New");
      run.setFontSize(10);
      run.setText(".........................................................................................");
      setLineSpacing(line, 0);
     
      XWPFParagraph p_refno_date = document.createParagraph();
      XWPFRun run2 = p_refno_date.createRun();
      p_refno_date.setAlignment(ParagraphAlignment.LEFT);
     
      run2.setFontFamily("Courier New");
      run2.setFontSize(12);
      run2.setBold(true);
      if (!duplicateCopy) {
        run2.setText("Ref No. : " + ((PrintOurBankVO)printVO.getPrintList().get(0)).getTransrefno() +
          "\t                              Date : " + ((PrintOurBankVO)printVO.getPrintList().get(0)).getSystemDate());
      } else if (duplicateCopy) {
        run2.setText("Ref No. : " + ((PrintOurBankVO)printVO.getPrintList().get(0)).getTransrefno() +
          "\t     (Duplicate Copy)         Date : " + ((PrintOurBankVO)printVO.getPrintList().get(0)).getSystemDate());
      }
      XWPFParagraph p_topic = document.createParagraph();
      p_topic.setAlignment(ParagraphAlignment.CENTER);
      XWPFRun run3 = p_topic.createRun();
      run3.setFontFamily("Courier New");
      run3.setFontSize(10);
      run3.setBold(true);
      if (type.equalsIgnoreCase("FIRC")) {
        run3.setText("CERTIFICATE OF FOREIGN INWARD REMITTANCE");
      } else if (type.equalsIgnoreCase("FIRA")) {
        run3.setText("CERTIFICATE OF FOREIGN INWARD ADVICE");
      }
      XWPFParagraph paragraph_4 = document.createParagraph();
      XWPFRun run4 = paragraph_4.createRun();
      run4.setText(" \tWe certify that we have received the following remittance and proceeds thereof \twere paid to :");
     
      run4.setFontFamily("Courier New");
      run4.setFontSize(10);
     
      XWPFParagraph paragraph_5 = document.createParagraph();
      XWPFRun run5 = paragraph_5.createRun();
      run5.setText("(a)  To the beneficiary   ");
      run5.setFontFamily("Courier New");
      run5.setFontSize(10);
      run5.setBold(true);
      if (benAddress == 1)
      {
        XWPFRun run51 = paragraph_5.createRun();
        run51.setText(beneficiary);
        run51.setFontFamily("Courier New");
        run51.setFontSize(10);
        run51.addBreak();
      }
      else if (benAddress == 2)
      {
        XWPFRun run51 = paragraph_5.createRun();
        run51.setText(beneficiary.substring(0, 48));
        run51.setFontFamily("Courier New");
        run51.setFontSize(10);
        run51.addBreak();
       
        XWPFRun run511 = paragraph_5.createRun();
        run511.setText(" \t\t\t\t  " + beneficiary.substring(48, beneficiary.length()));
        run511.setFontFamily("Courier New");
        run511.setFontSize(10);
      }
      else if (benAddress == 3)
      {
        XWPFRun run51 = paragraph_5.createRun();
        run51.setText(beneficiary.substring(0, 48));
        run51.setFontFamily("Courier New");
        run51.setFontSize(10);
        run51.addBreak();
       
        XWPFRun run511 = paragraph_5.createRun();
        run511.setText(" \t\t\t\t  " + beneficiary.substring(48, 96));
        run511.setFontFamily("Courier New");
        run511.setFontSize(10);
        run511.addBreak();
       
        XWPFRun run512 = paragraph_5.createRun();
        run512.setText(" \t\t\t\t  " + beneficiary.substring(96, beneficiary.length()));
        run512.setFontFamily("Courier New");
        run512.setFontSize(10);
      }
      XWPFParagraph paragraph_6 = document.createParagraph();
      XWPFRun run6 = paragraph_6.createRun();
      run6.setText(" \t By Credit to Current/Saving/Cash Credit Account with us " + printVO.getAccNo());
      run6.setFontFamily("Courier New");
      run6.setFontSize(10);
     
      XWPFParagraph paragraph_7 = document.createParagraph();
      XWPFRun run7 = paragraph_7.createRun();
      run7.setText(" \t DATE OF CREDIT :\t " + ((PrintOurBankVO)printVO.getPrintList().get(0)).getValue_date());
      run7.setFontFamily("Courier New");
      run7.setFontSize(10);
      run7.addBreak();
     
      XWPFParagraph paragraph_8 = document.createParagraph();
      XWPFRun run8 = paragraph_8.createRun();
      run8.setText("(b)  To Bank on for credit of beneficiary's Account");
      run8.setFontFamily("Courier New");
      run8.setFontSize(10);
      run8.setBold(true);
     

      orderingCustomer = ((PrintOurBankVO)printVO.getPrintList().get(0)).getOrderingcustomer();
      if ((orderingCustomer != null) && (orderingCustomer.trim().length() > 0)) {
        ordCustAddrLen = orderingCustomer.length();
      }
      if (ordCustAddrLen > 48) {
        ordCustAddress = 2;
      } else {
        ordCustAddress = 1;
      }
      if (ordCustAddrLen > 96) {
        ordCustAddress = 3;
      }
      logger.info("Ordering Customer address: " + orderingCustomer);
     
      XWPFParagraph paragraph_9 = document.createParagraph();
      if (ordCustAddress == 1)
      {
        XWPFRun run9 = paragraph_9.createRun();
        run9.setText(" \t Name and Place of Residence of Remitter\t: " + orderingCustomer);
        run9.setFontFamily("Courier New");
        run9.setFontSize(10);
      }
      else if (ordCustAddress == 2)
      {
        XWPFRun run51 = paragraph_9.createRun();
        run51.setText(" \t Name and Place of Residence of Remitter\t: " + orderingCustomer.substring(0, 30));
        run51.setFontFamily("Courier New");
        run51.setFontSize(10);
        run51.addBreak();
       
        XWPFRun run511 = paragraph_9.createRun();
        run511.setText(" \t\t\t\t\t\t\t\t\t  " + orderingCustomer.substring(30, orderingCustomer.length()));
        run511.setFontFamily("Courier New");
        run511.setFontSize(10);
      }
      else if (ordCustAddress == 3)
      {
        XWPFRun run51 = paragraph_9.createRun();
        run51.setText(" \t Name and Place of Residence of Remitter\t: " + orderingCustomer.substring(0, 30));
        run51.setFontFamily("Courier New");
        run51.setFontSize(10);
        run51.addBreak();
       
        XWPFRun run511 = paragraph_9.createRun();
        run511.setText(" \t\t\t\t\t\t\t\t\t  " + orderingCustomer.substring(30, 60));
        run511.setFontFamily("Courier New");
        run511.setFontSize(10);
        run511.addBreak();
       
        XWPFRun run512 = paragraph_9.createRun();
        run512.setText(" \t\t\t\t\t\t\t\t\t  " + orderingCustomer.substring(60, orderingCustomer.length()));
        run512.setFontFamily("Courier New");
        run512.setFontSize(10);
      }
      remittingBank = ((PrintOurBankVO)printVO.getPrintList().get(0)).getRemmitngbankdetails();
      if ((remittingBank != null) && (remittingBank.trim().length() > 0)) {
        remBankLen = remittingBank.length();
      }
      if (remBankLen > 30) {
        remAddress = 2;
      } else {
        remAddress = 1;
      }
      if (remBankLen > 60) {
        remAddress = 3;
      }
      XWPFParagraph paragraph_10 = document.createParagraph();
      if (remAddress == 1)
      {
        XWPFRun run10 = paragraph_10.createRun();
        run10.setText(" \t Name and Place of Remitting bank\t      :\t" + remittingBank);
        run10.setFontFamily("Courier New");
        run10.setFontSize(10);
      }
      else if (remAddress == 2)
      {
        XWPFRun run51 = paragraph_10.createRun();
        run51.setText(" \t Name and Place of Remitting bank\t      :\t" + remittingBank.substring(0, 30));
        run51.setFontFamily("Courier New");
        run51.setFontSize(10);
        run51.addBreak();
       
        XWPFRun run511 = paragraph_10.createRun();
        run511.setText(" \t\t\t\t\t\t\t\t\t  " + remittingBank.substring(30, remittingBank.length()));
        run511.setFontFamily("Courier New");
        run511.setFontSize(10);
      }
      else if (remAddress == 3)
      {
        XWPFRun run51 = paragraph_10.createRun();
        run51.setText(" \t Name and Place of Remitting bank\t      :\t" + remittingBank.substring(0, 30));
        run51.setFontFamily("Courier New");
        run51.setFontSize(10);
        run51.addBreak();
       
        XWPFRun run511 = paragraph_10.createRun();
        run511.setText(" \t\t\t\t\t\t\t\t\t  " + remittingBank.substring(30, 60));
        run511.setFontFamily("Courier New");
        run511.setFontSize(10);
        run511.addBreak();
       
        XWPFRun run512 = paragraph_10.createRun();
        run512.setText(" \t\t\t\t\t\t\t\t\t  " + remittingBank.substring(60, remittingBank.length()));
        run512.setFontFamily("Courier New");
        run512.setFontSize(10);
      }
      XWPFParagraph paragraph_111 = document.createParagraph();
      XWPFRun run111 = paragraph_111.createRun();
      run111.setText(" \t DD/TT/MT No\t   :\t" + ((PrintOurBankVO)printVO.getPrintList().get(0)).getNostroNo() +
        "\t  Dated : " + ((PrintOurBankVO)printVO.getPrintList().get(0)).getNostroDate());
      run111.setFontFamily("Courier New");
      run111.setFontSize(10);
     
      XWPFParagraph paragraph_11 = document.createParagraph();
      XWPFRun run11 = paragraph_11.createRun();
      run11.setText(" \t Foreign Currency Amount");
      run11.setFontFamily("Courier New");
      run11.setFontSize(10);
      run11.setBold(true);
     
      String moneyString = null;
      BigDecimal amount = new BigDecimal(0);BigDecimal exRate = null;
      if (((PrintOurBankVO)printVO.getAccList().get(0)).getFircEX() != null)
      {
        exRate = new BigDecimal(((PrintOurBankVO)printVO.getAccList().get(0)).getFircEX());
        if (!exRate.equals(Integer.valueOf(1)))
        {
          amount = amount.add(new BigDecimal(((PrintOurBankVO)printVO.getAccList().get(0)).getFircConAmt().replaceAll(",", "")));
         
          DecimalFormat formatter = new DecimalFormat("##,##,###.00");
         
          moneyString = formatter.format(amount);
        }
      }
      if (((PrintOurBankVO)printVO.getAccList().get(0)).getFircCurrRec() != null) {
        if (!((PrintOurBankVO)printVO.getAccList().get(0)).getFircCurr().equals("INR"))
        {
          XWPFRun run113 = paragraph_11.createRun();
          run113.setText("    " + ((PrintOurBankVO)printVO.getPrintList().get(0)).getCurrency() + " " + ((PrintOurBankVO)printVO.getPrintList().get(0)).getAmount() +
            " and EEFC Amount " + ((PrintOurBankVO)printVO.getPrintList().get(0)).getAmount());
          run113.setFontFamily("Courier New");
          run113.setFontSize(10);
        }
        else
        {
          XWPFRun run113 = paragraph_11.createRun();
          run113.setText("    " + ((PrintOurBankVO)printVO.getPrintList().get(0)).getCurrency() + " " + ((PrintOurBankVO)printVO.getPrintList().get(0)).getAmount() +
            " and EEFC Amount 0.00");
          run113.setFontFamily("Courier New");
          run113.setFontSize(10);
         
          XWPFParagraph paragraph_112 = document.createParagraph();
          XWPFRun run112 = paragraph_112.createRun();
          run112.setText(" \t\t Equivalent to : INR " + moneyString);
          run112.setFontFamily("Courier New");
          run112.setFontSize(10);
        }
      }
      XWPFParagraph paragraph_12 = document.createParagraph();
      XWPFRun run12 = paragraph_12.createRun();
      run12.setText(" \t Currency Conversion Details are as below :");
      run12.setFontFamily("Courier New");
      run12.setFontSize(10);
     
      XWPFTable table = document.createTable(2, 5);
     
      setTableAlign(table, ParagraphAlignment.CENTER);
     
      XWPFTableRow rowOne = table.getRow(0);
     
      XWPFParagraph p1 = rowOne.getCell(0).addParagraph();
      setRun(p1.createRun(), "Courier New", 10, " From Currency", false, false);
      setLineSpacing(p1, 0);
     
      XWPFParagraph p2 = rowOne.getCell(1).addParagraph();
      setRun(p2.createRun(), "Courier New", 10, " Amount", false, false);
      setLineSpacing(p2, 0);
     
      XWPFParagraph p3 = rowOne.getCell(2).addParagraph();
      setRun(p3.createRun(), "Courier New", 10, " Rate", false, false);
      setLineSpacing(p3, 0);
     
      XWPFParagraph p4 = rowOne.getCell(3).addParagraph();
      setRun(p4.createRun(), "Courier New", 10, " To Currency", false, false);
      setLineSpacing(p4, 0);
     
      XWPFParagraph p5 = rowOne.getCell(4).addParagraph();
      setRun(p5.createRun(), "Courier New", 10, " Amount", false, false);
      setLineSpacing(p5, 0);
     
      XWPFTableRow rowTwo = table.getRow(1);
      XWPFParagraph p11 = rowTwo.getCell(0).addParagraph();
      setRun(p11.createRun(), "Courier New", 10, " " + ((PrintOurBankVO)printVO.getAccList().get(0)).getFircCurrRec(), false, false);
      setLineSpacing(p11, 0);
     
      XWPFParagraph p21 = rowTwo.getCell(1).addParagraph();
      setRun(p21.createRun(), "Courier New", 10, " " + ((PrintOurBankVO)printVO.getAccList().get(0)).getFircAmount(), false, false);
      setLineSpacing(p21, 0);
     
      XWPFParagraph p31 = rowTwo.getCell(2).addParagraph();
      setRun(p31.createRun(), "Courier New", 10, " " + exRate.setScale(4, RoundingMode.HALF_EVEN), false, false);
      setLineSpacing(p31, 0);
     
      XWPFParagraph p41 = rowTwo.getCell(3).addParagraph();
      setRun(p41.createRun(), "Courier New", 10, " " + ((PrintOurBankVO)printVO.getAccList().get(0)).getFircCurr(), false, false);
      setLineSpacing(p41, 0);
     
      XWPFParagraph p51 = rowTwo.getCell(4).addParagraph();
      setRun(p51.createRun(), "Courier New", 10, " " + ((PrintOurBankVO)printVO.getAccList().get(0)).getFircConAmt(), false, false);
      setLineSpacing(p51, 0);
     
      table.getRow(0).getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1750L));
      table.getRow(0).getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1750L));
      table.getRow(0).getCell(2).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1750L));
      table.getRow(0).getCell(3).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1750L));
      table.getRow(0).getCell(4).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(1750L));
     
      XWPFParagraph paragraph_13 = document.createParagraph();
      XWPFRun run13 = paragraph_13.createRun();
      run13.addBreak();
      run13.setText(" \tPurpose of remittance as per Beneficiary / Remitter :");
      run13.setFontFamily("Courier New");
      run13.setFontSize(10);
      run13.setBold(true);
     

      purposeDesc = ((PrintOurBankVO)printVO.getPrintList().get(0)).getPurposedesc();
      int purpDescLength = purposeDesc.length();
      if (purpDescLength > 25) {
        purpDesc = 2;
      } else {
        purpDesc = 1;
      }
      if (purpDescLength > 77) {
        purpDesc = 3;
      }
      if (purpDesc == 1)
      {
        XWPFRun run131 = paragraph_13.createRun();
        run131.setText(" " + purposeDesc);
        run131.setFontFamily("Courier New");
        run131.setFontSize(10);
        run131.addBreak();
        setLineSpacing(paragraph_13, 0);
      }
      else if (purpDesc == 2)
      {
        XWPFRun run131 = paragraph_13.createRun();
        run131.setText(" " + purposeDesc.substring(0, 25));
        run131.setFontFamily("Courier New");
        run131.setFontSize(10);
        run131.addBreak();
        setLineSpacing(paragraph_13, 0);
       
        XWPFRun run132 = paragraph_13.createRun();
        run132.setText(" \t" + purposeDesc.substring(25, purposeDesc.length()));
        run132.setFontFamily("Courier New");
        run132.setFontSize(10);
        run132.addBreak();
        setLineSpacing(paragraph_13, 0);
      }
      else if (purpDesc == 3)
      {
        XWPFRun run131 = paragraph_13.createRun();
        run131.setText(" " + purposeDesc.substring(0, 25));
        run131.setFontFamily("Courier New");
        run131.setFontSize(10);
        run131.addBreak();
        setLineSpacing(paragraph_13, 0);
       
        XWPFRun run132 = paragraph_13.createRun();
        run132.setText(" \t" + purposeDesc.substring(25, 77));
        run132.setFontFamily("Courier New");
        run132.setFontSize(10);
        run132.addBreak();
        setLineSpacing(paragraph_13, 0);
       
        XWPFRun run133 = paragraph_13.createRun();
        run133.setText(" \t" + purposeDesc.substring(77, purposeDesc.length()));
        run133.setFontFamily("Courier New");
        run133.setFontSize(10);
        run133.addBreak();
        setLineSpacing(paragraph_13, 0);
      }
      XWPFParagraph paragraph_15 = document.createParagraph();
      XWPFRun run15 = paragraph_15.createRun();
      run15.setText(" \tWe also certify that the payment thereof has / has not been received in non- \tconvertible rupees or under any special trade or payments agreement.");
     
      run15.setFontFamily("Courier New");
      run15.setFontSize(10);
      setLineSpacing(paragraph_15, 0);
      run15.addBreak();
     
      XWPFParagraph paragraph_16 = document.createParagraph();
      XWPFRun run16 = paragraph_16.createRun();
      run16.setText(" \tWe confirm that we have obtained reimbursement in an approved manner.");
      run16.setFontFamily("Courier New");
      run16.setFontSize(10);
      setLineSpacing(paragraph_16, 0);
      run16.addBreak();
     
      XWPFParagraph paragraph_17 = document.createParagraph();
      XWPFRun run17 = paragraph_17.createRun();
      run17.setText(" \tPurpose if advance export then the shipment of goods should be made within one \tyear from date of receipt of advance payment.");
     
      run17.setFontFamily("Courier New");
      run17.setFontSize(10);
      setLineSpacing(paragraph_17, 0);
      run17.addBreak();
      if (type.equalsIgnoreCase("FIRC"))
      {
        XWPFParagraph paragraph_18 = document.createParagraph();
        XWPFRun run18 = paragraph_18.createRun();
        run18.setText(" \tPlease Note that the Validity of this Certificate is One Year from the Date of \tIssuance.");
       
        run18.setFontFamily("Courier New");
        setLineSpacing(paragraph_18, 0);
        run18.setFontSize(10);
       
        XWPFParagraph paragraph_19 = document.createParagraph();
        XWPFRun run19 = paragraph_19.createRun();
        run19.setText("                                                             Countersigned");
        run19.setFontFamily("Courier New");
        run19.setFontSize(10);
        run19.setBold(true);
       
        XWPFParagraph paragraph_20 = document.createParagraph();
        XWPFRun run20 = paragraph_20.createRun();
        run20.setText("For KOTAK MAHINDRA BANK LTD                                  For KOTAK MAHINDRA BANK LTD");
        run20.setFontFamily("Courier New");
        run20.setFontSize(10);
        run20.setBold(true);
        run20.addBreak();
        run20.addBreak();
       
        XWPFParagraph paragraph_21 = document.createParagraph();
        XWPFRun run21 = paragraph_21.createRun();
        run21.setText("Authorised Signatory                                         Authorised Signatory");
        run21.setFontFamily("Courier New");
        run21.setFontSize(10);
        run21.setBold(true);
      }
      else if (type.equalsIgnoreCase("FIRA"))
      {
        XWPFParagraph paragraph_18 = document.createParagraph();
        XWPFRun run18 = paragraph_18.createRun();
        run18.setText(" \tThis is system generated advice and no signature is required.");
       
        run18.setFontFamily("Courier New");
        setLineSpacing(paragraph_18, 0);
        run18.setFontSize(10);
      }
      document.write(baos);
     
      logger.info("Document written successfully");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
 
  private static void setRun(XWPFRun run, String fontFamily, int fontSize, String text, boolean bold, boolean addBreak)
  {
    run.setFontFamily(fontFamily);
    run.setFontSize(fontSize);
    run.setText(text);
    run.setBold(bold);
    if (addBreak) {
      run.addBreak();
    }
  }
 
  private static void setLineSpacing(XWPFParagraph paragraph, int spacing)
  {
    paragraph.setSpacingAfter(spacing);
    paragraph.setSpacingAfterLines(spacing);
    paragraph.setSpacingBefore(spacing);
    paragraph.setSpacingBeforeLines(spacing);
    paragraph.setSpacingLineRule(LineSpacingRule.EXACT);
    paragraph.setVerticalAlignment(TextAlignment.TOP);
  }
 
  public static void setTableAlign(XWPFTable table, ParagraphAlignment align)
  {
    CTTblPr tblPr = table.getCTTbl().getTblPr();
    CTJc jc = tblPr.isSetJc() ? tblPr.getJc() : tblPr.addNewJc();
    STJc.Enum en = STJc.Enum.forInt(align.getValue());
    jc.setVal(en);
  }
}
