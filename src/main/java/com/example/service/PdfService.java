package com.example.service;

import com.example.model.PurchaseOrder;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final MerchantService merchantService;

    public byte[] generateInvoice(String customerName, double totalAmount, List<PurchaseOrder> POs) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 80, 60);
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            // Header & Footer
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            writer.setPageEvent(event);

            document.open();

            // Fonts
            BaseFont bf = BaseFont.createFont(
                    "src/main/resources/fonts/Amiri-Regular.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font arabicFont = new Font(bf, 12);
            Font arabicFontBold = new Font(bf, 14, Font.BOLD);
            Font titleFont = new Font(bf, 20, Font.BOLD);

            // ==== Title ====
            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setWidthPercentage(100);
            titleTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            PdfPCell titleCell = new PdfPCell(new Phrase("فاتورة مبيعات (Sales Invoice)", titleFont));
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleTable.addCell(titleCell);
            document.add(titleTable);
            document.add(Chunk.NEWLINE);

            // ==== Invoice Info ====
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new int[]{2, 1});
            infoTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            infoTable.addCell(createCell("اسم الزبون: " + customerName, arabicFont, Element.ALIGN_LEFT, false));
            infoTable.addCell(createCell("التاريخ: " + LocalDate.now(), arabicFont, Element.ALIGN_LEFT, false));

            document.add(infoTable);
            document.add(Chunk.NEWLINE);

            // ==== Table Header ====
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 1, 1});
            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            table.addCell(createHeaderCell("التاريخ", arabicFontBold));
            table.addCell(createHeaderCell("المجموع (JOD)", arabicFontBold));
            table.addCell(createHeaderCell("رقم الطلب (PO ID)", arabicFontBold));

            // ==== Table Content ====
            for (PurchaseOrder po : POs) {
                table.addCell(createCell(po.getDate().toString(), arabicFont, Element.ALIGN_CENTER, true));
                table.addCell(createCell(String.format("%.2f", po.getTotalPrice()), arabicFont, Element.ALIGN_CENTER, true));
                table.addCell(createCell(String.valueOf(po.getId()), arabicFont, Element.ALIGN_CENTER, true));
            }
            document.add(table);
            document.add(Chunk.NEWLINE);

            // ==== Total Section (FIXED RTL/LTR MIX) ====
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(50);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            PdfPCell labelCell = new PdfPCell(new Phrase("المجموع الكلي:", arabicFontBold));
            labelCell.setBorder(Rectangle.NO_BORDER);
            labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            PdfPCell valueCell = new PdfPCell(new Phrase(String.format("%.2f JOD", totalAmount), arabicFontBold));
            valueCell.setBorder(Rectangle.NO_BORDER);
            valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            totalTable.addCell(labelCell);
            totalTable.addCell(valueCell);
            document.add(totalTable);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate invoice PDF", e);
        }
    }

    // --- Helpers ---
    private PdfPCell createHeaderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(8);
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        return cell;
    }

    private PdfPCell createCell(String text, Font font, int alignment, boolean border) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(6);
        if (!border) cell.setBorder(Rectangle.NO_BORDER);
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        return cell;
    }

    // --- Header & Footer ---
    class HeaderFooterPageEvent extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                // ==== Header ====
                PdfPTable header = new PdfPTable(2);
                header.setTotalWidth(527);
                header.setWidths(new int[]{1, 3});
                header.setLockedWidth(true);
                header.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

                try {
                    Image logo = Image.getInstance("src/main/resources/images/darkGreenLogo.png");
                    logo.scaleToFit(60, 60);
                    PdfPCell logoCell = new PdfPCell(logo);
                    logoCell.setBorder(Rectangle.NO_BORDER);
                    logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    header.addCell(logoCell);
                } catch (Exception e) {
                    header.addCell(new PdfPCell(new Phrase("")));
                }

                BaseFont bf = BaseFont.createFont("src/main/resources/fonts/Amiri-Regular.ttf",
                        BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                Font headerFont = new Font(bf, 12);

                PdfPCell textCell = new PdfPCell(new Phrase(
                        "شركة الماسة للتمور\nAmman, Jordan\n+962-7-XXXXXXX\ninfo@almasahdates.com",
                        headerFont));
                textCell.setBorder(Rectangle.NO_BORDER);
                textCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                header.addCell(textCell);

                header.writeSelectedRows(0, -1, 34, 830, writer.getDirectContent());

                // ==== Footer ====
                PdfPTable footer = new PdfPTable(1);
                footer.setTotalWidth(527);
                footer.setLockedWidth(true);
                footer.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

                Font footerFont = new Font(bf, 10);
                PdfPCell footerCell = new PdfPCell(
                        new Phrase("شكراً لتعاملكم معنا! (Thank you for your business)", footerFont));
                footerCell.setBorder(Rectangle.TOP);
                footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                footer.addCell(footerCell);

                footer.writeSelectedRows(0, -1, 34, 50, writer.getDirectContent());
            } catch (Exception ignored) {
            }
        }
    }
}
