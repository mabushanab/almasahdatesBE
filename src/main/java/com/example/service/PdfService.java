package com.example.service;

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
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final MerchantService merchantService;

    // =================== SALE INVOICE ===================
    public byte[] generateInvoiceSOs(String customerName, List<SaleOrder> saleOrders) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 80, 60);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.setPageEvent(new HeaderFooterPageEvent());
            document.open();

            BaseFont bf = BaseFont.createFont("src/main/resources/fonts/Amiri-Regular.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font normal = new Font(bf, 12);
            Font bold = new Font(bf, 14, Font.BOLD);
            Font title = new Font(bf, 20, Font.BOLD);

            // ==== Title ====
            addCenteredTitle(document, "فاتورة مبيعات (Sales Invoice)", title);

            // ==== Info ====
            addInfoSection(document, "اسم الزبون: " + customerName,
                    "التاريخ: " + LocalDate.now(), normal);

            // ==== Table ====
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3f, 1.5f, 1.5f, 1.5f});
            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            table.addCell(createHeaderCell("المنتج", bold));
            table.addCell(createHeaderCell("السعر الفردي", bold));
            table.addCell(createHeaderCell("الكمية", bold));
            table.addCell(createHeaderCell("الإجمالي", bold));

            List<Products> products = saleOrders.get(0).getProducts();
            for (Products product : products) {
                table.addCell(createWrappedCell(product.getItem().getName(), normal));
                table.addCell(createWrappedCell(String.format("%.2f", product.getPriceForItem()), normal));
                table.addCell(createWrappedCell(String.valueOf(product.getQuantity()), normal));
                table.addCell(createWrappedCell(
                        String.format("%.2f", product.getPriceForItem() * product.getQuantity()), normal));
            }

            double totalAmount = products.stream().mapToDouble(p -> p.getQuantity() * p.getPriceForItem()).sum();
            document.add(table);
            document.add(Chunk.NEWLINE);

            addTotalSection(document, totalAmount, bold);
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate sales invoice PDF", e);
        }
    }

    // =================== PURCHASE INVOICE ===================
    public byte[] generateInvoice(String customerName, double totalAmount, List<PurchaseOrder> purchaseOrders) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 80, 60);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.setPageEvent(new HeaderFooterPageEvent());
            document.open();

            BaseFont bf = BaseFont.createFont("src/main/resources/fonts/Amiri-Regular.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font normal = new Font(bf, 12);
            Font bold = new Font(bf, 14, Font.BOLD);
            Font title = new Font(bf, 20, Font.BOLD);

            addCenteredTitle(document, "فاتورة مشتريات (Purchase Invoice)", title);
            addInfoSection(document, "اسم المورد: " + customerName,
                    "التاريخ: " + LocalDate.now(), normal);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 1.5f, 1.5f});
            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            table.addCell(createHeaderCell("التاريخ", bold));
            table.addCell(createHeaderCell("المجموع (JOD)", bold));
            table.addCell(createHeaderCell("رقم الطلب", bold));

            for (PurchaseOrder po : purchaseOrders) {
                table.addCell(createWrappedCell(po.getDate().toString(), normal));
                table.addCell(createWrappedCell(String.format("%.2f", po.getTotalPrice()), normal));
                table.addCell(createWrappedCell(String.valueOf(po.getId()), normal));
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            addTotalSection(document, totalAmount, bold);
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate purchase invoice PDF", e);
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

    private void addTotalSection(Document document, double total, Font font) throws DocumentException {
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(40);
        totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        PdfPCell label = new PdfPCell(new Phrase("المجموع الكلي:", font));
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
        cell.setNoWrap(false); // allow wrapping long text
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

                try {
                    Image logo = Image.getInstance("src/main/resources/images/darkGreenLogo.png");
                    logo.scaleToFit(60, 60);
                    PdfPCell logoCell = new PdfPCell(logo);
                    logoCell.setBorder(Rectangle.NO_BORDER);
                    logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    header.addCell(logoCell);
                } catch (Exception e) {
                    header.addCell(new PdfPCell(new Phrase(" ")));
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
