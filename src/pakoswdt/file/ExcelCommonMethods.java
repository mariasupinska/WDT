package pakoswdt.file;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import pakoswdt.model.Buyer;
import pakoswdt.model.Invoice;
import pakoswdt.model.Product;
import pakoswdt.model.Seller;

import java.util.Comparator;
import java.util.List;

public abstract class ExcelCommonMethods {
    protected final HSSFWorkbook hssfWorkbook;
    protected final Invoice invoice;
    protected final List<Product> products;

    protected final Comparator<Product> productPackageComparator = Comparator.comparing(Product::productPackageType)
            .thenComparing(Product::productPackageAmount)
            .thenComparing(Product::productPackageWeight);

    static final int SUBHEADER_ROW = 2;
    static final int NAME_ROW = 5;
    static final int ADDRESS_ROW = 6;
    static final int ADDRESS_2_ROW = 7;

    static final int SELLER_COL_START = 1;
    static final int BUYER_COL_START = 6;
    static final int LEFT_SUBHEADER_COL_START = 0;
    static final int RIGHT_SUBHEADER_COL_START = 8;

    static final CellRangeAddress HEADER_REGION = new CellRangeAddress(0, 1, 0, 11);
    static final CellRangeAddress LEFT_SUBHEADER_REGION = new CellRangeAddress(2, 2, LEFT_SUBHEADER_COL_START, RIGHT_SUBHEADER_COL_START - 1);
    static final CellRangeAddress RIGHT_SUBHEADER_REGION = new CellRangeAddress(2, 2, RIGHT_SUBHEADER_COL_START, 11);

    static final CellRangeAddress SELLER_NAME_REGION = new CellRangeAddress(NAME_ROW, NAME_ROW, 1, 4);
    static final CellRangeAddress SELLER_ADDRESS_REGION = new CellRangeAddress(ADDRESS_ROW, ADDRESS_ROW, 1, 4);
    static final CellRangeAddress SELLER_ADDRESS_2_REGION = new CellRangeAddress(ADDRESS_2_ROW, ADDRESS_2_ROW, 1, 4);

    static final CellRangeAddress BUYER_NAME_REGION = new CellRangeAddress(NAME_ROW, NAME_ROW, 6, 10);
    static final CellRangeAddress BUYER_ADDRESS_REGION = new CellRangeAddress(ADDRESS_ROW, ADDRESS_ROW, 6, 10);
    static final CellRangeAddress BUYER_ADDRESS_2_REGION = new CellRangeAddress(ADDRESS_2_ROW, ADDRESS_2_ROW, 6, 10);

    protected ExcelCommonMethods(HSSFWorkbook hssfWorkbook, Invoice invoice, List<Product> products) {
        this.hssfWorkbook = hssfWorkbook;
        this.invoice = invoice;
        this.products = products;
    }

    void setPrintSetup(Sheet sheet) {
        sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
        sheet.getPrintSetup().setFitWidth((short) 1);
        sheet.getPrintSetup().setFitHeight((short) 0);
        sheet.setAutobreaks(true);
    }

    void setHeaderText(Sheet sheet, String header) {
        CellStyle headerStyle = hssfWorkbook.createCellStyle();
        headerStyle.setFont(createHeaderFont());
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headerStyle.setWrapText(true);

        Row row = sheet.createRow((short) 0);
        sheet.addMergedRegion(HEADER_REGION);
        Cell cell = row.createCell(0);
        cell.setCellValue(header);
        cell.setCellStyle(headerStyle);
    }

    void setInvoiceText(Sheet sheet) {
        Row row = sheet.createRow(SUBHEADER_ROW);

        //left part
        CellStyle rightAlignCellStyle = createRightAlignedCellStyle();

        String leftText = "Dotyczy wewnątrzwspólnotowej dostawy towarów udokumentowanej fakturą ";
        addMergedRegionContent(sheet, row, LEFT_SUBHEADER_REGION, rightAlignCellStyle, LEFT_SUBHEADER_COL_START, leftText);

        //right part
        CellStyle leftAlignCellStyle = createLeftAlignedBoldCellStyle().build();

        String rightText = "nr " + invoice.getNumber().get() + " z dnia " + invoice.getCreationDate().get().toString();
        addMergedRegionContent(sheet, row, RIGHT_SUBHEADER_REGION, leftAlignCellStyle, RIGHT_SUBHEADER_COL_START, rightText);

    }

    void setBusinessText(Sheet sheet) {
        CellStyle businessCellStyle = new CellStyleBuilder(hssfWorkbook).align(CellStyle.ALIGN_LEFT).underline(Font.U_SINGLE).build();

        Row row = sheet.createRow((short) 4);

        Cell sellerCell = row.createCell(SELLER_COL_START);
        sellerCell.setCellValue("Sprzedawca:");
        sellerCell.setCellStyle(businessCellStyle);

        Cell buyerCell = row.createCell(BUYER_COL_START);
        buyerCell.setCellValue("Nabywca:");
        buyerCell.setCellStyle(businessCellStyle);

        CellStyle cellStyle = hssfWorkbook.createCellStyle();
        cellStyle.setFont(createStandardBoldFont());

        row = sheet.createRow(NAME_ROW);

        Seller seller = invoice.getSeller();
        Buyer buyer = invoice.getBuyer();

        String sellerText = seller.getName().get();
        addMergedRegionContent(sheet, row, SELLER_NAME_REGION, cellStyle, SELLER_COL_START, sellerText);

        String buyerText = buyer.getName().get();
        addMergedRegionContent(sheet, row, BUYER_NAME_REGION, cellStyle, BUYER_COL_START, buyerText);

        row = sheet.createRow(ADDRESS_ROW);

        sellerText = "ul. " + seller.getStreet().get() + ", " + seller.getPostalCode().get() + " " + seller.getCity().get();
        addMergedRegionContent(sheet, row, SELLER_ADDRESS_REGION, cellStyle, SELLER_COL_START, sellerText);

        buyerText = buyer.getCity().get() + ", " + buyer.getPostalCode().get() + " " + buyer.getCity().get();
        addMergedRegionContent(sheet, row, BUYER_ADDRESS_REGION, cellStyle, BUYER_COL_START, buyerText);

        row = sheet.createRow(ADDRESS_2_ROW);

        sellerText = seller.getCountry().get() + ", NIP UE " + seller.getNip().get();
        addMergedRegionContent(sheet, row, SELLER_ADDRESS_2_REGION, cellStyle, SELLER_COL_START, sellerText);

        buyerText = buyer.getCountry().get() + ", NIP UE " + buyer.getNip().get();
        addMergedRegionContent(sheet, row, BUYER_ADDRESS_2_REGION, cellStyle, BUYER_COL_START, buyerText);
    }

    void setAutosizeColumns(Sheet sheet) {
        for (int i = 0; i <= 11; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void addMergedRegionContent(Sheet sheet, Row row, CellRangeAddress mergedRegion, CellStyle cellStyle, int celNum, String content) {
        sheet.addMergedRegion(mergedRegion);
        Cell cell = row.createCell(celNum);
        cell.setCellValue(content);
        cell.setCellStyle(cellStyle);
    }

    Font createHeaderFont() {
        Font font = createStandardBoldFont();
        font.setFontHeightInPoints((short) 11);
        return font;
    }

    Font createStandardBoldFont() {
        Font font = createStandardFont();
        font.setBold(true);
        return font;
    }

    Font createStandardFont() {
        Font font = this.hssfWorkbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Helvetica");
        return font;
    }

    CellStyleBuilder createCenteredCellStyle() {
        return new CellStyleBuilder(hssfWorkbook)
                .align(CellStyle.ALIGN_CENTER)
                .verticalAlign(CellStyle.VERTICAL_CENTER)
                .wrapText(true);
    }

    CellStyle createRightAlignedCellStyle() {
        return new CellStyleBuilder(hssfWorkbook).align(CellStyle.ALIGN_RIGHT).verticalAlign(CellStyle.VERTICAL_CENTER).build();
    }

    CellStyleBuilder createLeftAlignedBoldCellStyle() {
        return new CellStyleBuilder(hssfWorkbook).bold(true).align(CellStyle.ALIGN_LEFT).verticalAlign(CellStyle.VERTICAL_CENTER);
    }

    CellStyle createBorderedCellStyle(CellStyle cellStyle) {
        short borderThin = CellStyle.BORDER_THIN;
        cellStyle.setBorderTop(borderThin);
        cellStyle.setBorderBottom(borderThin);
        cellStyle.setBorderLeft(borderThin);
        cellStyle.setBorderRight(borderThin);

        short index = IndexedColors.BLACK.getIndex();
        cellStyle.setTopBorderColor(index);
        cellStyle.setBottomBorderColor(index);
        cellStyle.setLeftBorderColor(index);
        cellStyle.setRightBorderColor(index);

        return cellStyle;
    }
}
