package com.example.service;

import com.example.model.Goods;
import com.example.model.Products;
import com.example.model.PurchaseOrder;
import com.example.model.SaleOrder;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfService {


    // =================== SALE INVOICE ===================
    public byte[] generateInvoiceSOs(SaleOrder saleOrders) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 80, 60);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.setPageEvent(new HeaderFooterPageEvent());
            document.open();

            BaseFont bf = loadBaseFont();
            Font normal = new Font(bf, 12);
            Font bold = new Font(bf, 14, Font.BOLD);
            Font title = new Font(bf, 20, Font.BOLD);

            // ==== Title ====
            addCenteredTitle(document, "فاتورة مبيعات (Sales Invoice)", title);

            // ==== Info ====
            addInfoSection(document, "اسم الزبون: " + saleOrders.getCustomer().getName(),
                    "   تاريخ الفاتورة: " + saleOrders.getDate() + "   التاريخ: " + LocalDate.now(), normal);

            // ==== Table ====
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 1.0f, 1.5f, 1.5f, 1.5f, 2.0f});
            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            table.addCell(createHeaderCell("المنتج", bold));
            table.addCell(createHeaderCell("الكمية", bold));
            table.addCell(createHeaderCell("السعر الفردي قبل الضريبة", bold));
            table.addCell(createHeaderCell("السعر الفردي بعد الضريبة", bold));
            table.addCell(createHeaderCell("الخصم (%)", bold));
            table.addCell(createHeaderCell("الإجمالي", bold));

            double totalBeforeDiscount = 0;
            boolean discount = true;
            List<Products> products = saleOrders.getProducts();
            for (Products product : products) {
                table.addCell(createWrappedCell(product.getItem().getName(), normal));
                table.addCell(createWrappedCell(String.valueOf(product.getQuantity()), normal));
                table.addCell(createWrappedCell(String.format("%.2f", product.getPriceForItem() / 1.02), normal));
                table.addCell(createWrappedCell(String.format("%.2f", product.getPriceForItem()), normal));
                table.addCell(createWrappedCell(String.valueOf(product.getDiscount()), normal));
                if (product.getDiscount() != 0 && discount)
                    discount = false;
                double d = product.getPriceForItem() * product.getQuantity();
                table.addCell(createWrappedCell(
                        String.format("%.2f", d - d * (product.getDiscount() / 100.0)), normal));
                totalBeforeDiscount += d;
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            if (!discount) {
                addTotalSection(document, totalBeforeDiscount, "المجموع قبل الخصم", normal);
                addTotalSection(document, saleOrders.getTotalPrice(), "المجموع بعد الخصم", normal);
            } else addTotalSection(document, totalBeforeDiscount, "المجموع: ", normal);

            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate sales invoice PDF", e);
        }
    }

    public byte[] generateInvoicePOs(PurchaseOrder purchaseOrder) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 80, 60);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.setPageEvent(new HeaderFooterPageEvent());
            document.open();

            BaseFont bf = loadBaseFont();
            Font normal = new Font(bf, 12);
            Font bold = new Font(bf, 14, Font.BOLD);
            Font title = new Font(bf, 20, Font.BOLD);

            // ==== Title ====
            addCenteredTitle(document, "فاتورة مشتريات (Purchase Invoice)", title);

            // ==== Info ====
            addInfoSection(document, "اسم المورد: " + purchaseOrder.getMerchant().getName(),
                    "   تاريخ الفاتورة: " + purchaseOrder.getDate() + "   التاريخ: " + LocalDate.now(), normal);

            // ==== Table ====
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 1.0f, 1.5f, 1.5f, 2.0f});
            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            table.addCell(createHeaderCell("المنتج", bold));
            table.addCell(createHeaderCell("الكمية", bold));
            table.addCell(createHeaderCell("السعر الفردي قبل الضريبة", bold));
            table.addCell(createHeaderCell("السعر الفردي بعد الضريبة", bold));
//            table.addCell(createHeaderCell("الخصم (%)", bold));
            table.addCell(createHeaderCell("الإجمالي", bold));
            List<Goods> goods = purchaseOrder.getGoods();
            for (Goods good : goods) {
                table.addCell(createWrappedCell(good.getItem().getName(), normal));
                table.addCell(createWrappedCell(String.valueOf(good.getWeightInGrams()), normal));
                table.addCell(createWrappedCell(String.format("%.2f", good.getPriceForGrams() / 1.02), normal));
                table.addCell(createWrappedCell(String.format("%.2f", good.getPriceForGrams()), normal));
//                table.addCell(createWrappedCell(good.getDiscount() + "%", normal));
                double d = good.getPriceForGrams() * good.getWeightInGrams();
                table.addCell(createWrappedCell(
                        String.format("%.2f", d - d * (good.getDiscount() / 100.0)), normal));
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            addTotalSection(document, purchaseOrder.getTotalPrice(), "المحموع الكلي: ", bold);
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate sales invoice PDF", e);
        }
    }

    // =================== FONT LOADER ===================
    private BaseFont loadBaseFont() throws IOException, DocumentException {
        try (InputStream fontStream = getClass().getResourceAsStream("/fonts/Amiri-Regular.ttf")) {
            if (fontStream == null) {
                throw new IOException("Font not found in /resources/fonts/Amiri-Regular.ttf");
            }
            byte[] fontBytes = fontStream.readAllBytes();
            return BaseFont.createFont("Amiri-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontBytes, null);
        }
    }

    // =================== HELPERS ===================

    private void addCenteredTitle(Document document, String text, Font font) throws DocumentException {
        PdfPTable titleTable = new PdfPTable(1);
        titleTable.setWidthPercentage(100);
        titleTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        PdfPCell titleCell = new PdfPCell(new Phrase(text, font));
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleTable.addCell(titleCell);
        document.add(titleTable);
        document.add(Chunk.NEWLINE);
    }

    private void addInfoSection(Document document, String right, String left, Font font) throws DocumentException {
        PdfPTable info = new PdfPTable(2);
        info.setWidthPercentage(100);
        info.setWidths(new float[]{2, 1});
        info.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        info.addCell(createCell(right, font, Element.ALIGN_RIGHT, false));
        info.addCell(createCell(left, font, Element.ALIGN_LEFT, false));
        document.add(info);
        document.add(Chunk.NEWLINE);
    }

    private void addTotalSection(Document document, double total, String s, Font font) throws DocumentException {
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(50);
        totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//        font.setSize(10);
        PdfPCell label = new PdfPCell(new Phrase(s, font));
        label.setBorder(Rectangle.NO_BORDER);
        label.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell value = new PdfPCell(new Phrase(String.format("%.2f JOD", total), font));
        value.setBorder(Rectangle.NO_BORDER);
        value.setHorizontalAlignment(Element.ALIGN_LEFT);

        totalTable.addCell(label);
        totalTable.addCell(value);
        document.add(totalTable);
    }

    private PdfPCell createHeaderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(8);
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        return cell;
    }

    private PdfPCell createCell(String text, Font font, int align, boolean border) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(align);
        cell.setPadding(6);
        if (!border) cell.setBorder(Rectangle.NO_BORDER);
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        return cell;
    }

    private PdfPCell createWrappedCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setNoWrap(false);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        return cell;
    }

    // =================== HEADER & FOOTER ===================
    class HeaderFooterPageEvent extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                PdfPTable header = new PdfPTable(2);
                header.setTotalWidth(527);
                header.setWidths(new int[]{1, 3});
                header.setLockedWidth(true);
                header.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

                // Load logo from classpath
                try (InputStream logoStream = getClass().getResourceAsStream("/images/darkGreenLogo.png")) {
                    if (logoStream != null) {
                        Image logo = Image.getInstance(logoStream.readAllBytes());
                        logo.scaleToFit(60, 60);
                        PdfPCell logoCell = new PdfPCell(logo);
                        logoCell.setBorder(Rectangle.NO_BORDER);
                        logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        header.addCell(logoCell);
                    } else {
                        header.addCell(new PdfPCell(new Phrase(" ")));
                    }
                }

                BaseFont bf = loadBaseFont();
                Font headerFont = new Font(bf, 12);

                PdfPCell textCell = new PdfPCell(new Phrase(
                        "شركة الماسة للتمور\nAmman, Jordan\n+962-7-XXXXXXX\ninfo@almasahdates.com",
                        headerFont));
                textCell.setBorder(Rectangle.NO_BORDER);
                textCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                header.addCell(textCell);

                header.writeSelectedRows(0, -1, 34, 830, writer.getDirectContent());

                // Footer
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
