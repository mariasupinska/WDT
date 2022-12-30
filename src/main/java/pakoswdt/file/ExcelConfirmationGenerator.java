package pakoswdt.file;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import pakoswdt.model.Buyer;
import pakoswdt.model.Invoice;
import pakoswdt.model.Product;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class ExcelConfirmationGenerator extends ExcelCommonMethods {
    protected ExcelConfirmationGenerator(HSSFWorkbook hssfWorkbook, Invoice invoice, List<Product> products) {
        super(hssfWorkbook, invoice, products);
    }

    public void createConfirmation(Sheet sheet) {
        setPrintSetup(sheet);
        setHeaderText(sheet, "POTWIERDZENIE OTRZYMANIA TOWARU PRZEZ NABYWCĘ \nNA TERYTORIUM " +
                "PAŃSTWA CZŁONKOWSKIEGO UE");
        setInvoiceText(sheet);
        setBusinessText(sheet);
        setDeliveryText(sheet);
        setListData(sheet);
        setConfirmationFooterText(sheet);
        setAutosizeColumns(sheet);
    }

    private void setDeliveryText(Sheet sheet) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 2);

        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 11));

        Buyer deliveryAddress = invoice.getBuyer();
        row.createCell(1).setCellValue("ADRES MIEJSCA DOSTAWY TOWARÓW: " +
                deliveryAddress.getDeliveryStreet().get() + ", " +
                deliveryAddress.getDeliveryPostalCode().get() + " " +
                deliveryAddress.getDeliveryCity().get() + ", " +
                deliveryAddress.getDeliveryCountry().get());

        row = sheet.createRow(sheet.getLastRowNum() + 2);

        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 11));

        row.createCell(1).setCellValue("OKREŚLENIE TOWARÓW I ICH ILOŚCI");
    }

    private void setListData(Sheet sheet) {
        for (int i = 0; i < products.size(); i++) {
            putBasicPackage(sheet, i);
        }
    }

    private void setConfirmationFooterText(Sheet sheet) {
        setConfirmationTransport(sheet);

        CellStyle centeredCellStyle = createCenteredCellStyle().build();
        CellStyle addressCellStyle = createCenteredCellStyle().bold(true).underline(Font.U_SINGLE).build();
        CellStyle borderedCenteredCellStyle = createCenteredCellStyle().bold(true).build();

        Row row = sheet.createRow(sheet.getLastRowNum() + 4);
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 11));
        row.createCell(0).setCellValue("Potwierdzam przyjęcie wymienionego powyżej towaru w miejscu: ");
        row.getCell(0).setCellStyle(centeredCellStyle);

        row = sheet.createRow(sheet.getLastRowNum() + 2);
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 11));
        row.createCell(0).setCellValue(invoice.getBuyer().getDeliveryStreet().get() + ", " +
                invoice.getBuyer().getDeliveryPostalCode().get() + " " +
                invoice.getBuyer().getDeliveryCity().get() + ", " +
                invoice.getBuyer().getDeliveryCountry().get());
        row.getCell(0).setCellStyle(addressCellStyle);

        row = sheet.createRow(sheet.getLastRowNum() + 3);
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 4));
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 6, 10));
        row.createCell(6).setCellValue(invoice.getBuyer().getDeliveryCity().get() + ", dn. " +
                invoice.getDeliveryDate().get());
        row.getCell(6).setCellStyle(borderedCenteredCellStyle);
        row.createCell(1).setCellValue(invoice.getBuyer().getPersonConfirming().get());
        row.getCell(1).setCellStyle(borderedCenteredCellStyle);

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 4));
        row.createCell(1).setCellValue("..........................................");
        row.getCell(1).setCellStyle(centeredCellStyle);

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 4));
        row.createCell(1).setCellValue("/ signature & stamp /");

        Font font = createStandardFont();
        font.setFontHeightInPoints((short) 8);

        CellStyle smallCellStyle = hssfWorkbook.createCellStyle();
        smallCellStyle.setFont(font);
        smallCellStyle.setAlignment(CellStyle.ALIGN_CENTER);

        row.getCell(1).setCellStyle(smallCellStyle);
    }

    private void setConfirmationTransport(Sheet sheet) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 2);

        CellStyle centeredCellStyle = createCenteredCellStyle().build();

        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 11));
        row.createCell(0).setCellValue("Do miejsca przeznaczenia towary zostały dostarczone samochodem");
        row.getCell(0).setCellStyle(centeredCellStyle);

        row = sheet.createRow(sheet.getLastRowNum() + 1);

        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 11));
        row.createCell(0).setCellValue(invoice.getTransport().getBrand().get() + " o numerze rejestracyjnym " + invoice.getTransport().getNumber().get());

        CellStyle cellStyle = createLeftAlignedBoldCellStyle().align(CellStyle.ALIGN_CENTER).build();
        row.getCell(0).setCellStyle(cellStyle);
    }

    private void putBasicPackage(Sheet sheet, int rowNumber) {
        Product product = products.get(rowNumber);

        sheet.addMergedRegion(new CellRangeAddress(rowNumber + 13, rowNumber + 13, 1, 5));

        CellStyle centeredBoldCellStyle = createCenteredCellStyle().bold(true).build();

        Row row = sheet.createRow(rowNumber + 13);
        row.createCell(0).setCellValue(rowNumber + 1);
        row.createCell(1).setCellValue(product.getName().get());
        row.createCell(6).setCellValue(BigDecimal.valueOf(product.getAmount().get()).setScale(2, BigDecimal.ROUND_HALF_UP).toString().replace(".", ","));
        row.createCell(7).setCellValue(product.getUnit().get());

        CellStyle standardCellStyle = hssfWorkbook.createCellStyle();
        standardCellStyle.setAlignment(CellStyle.ALIGN_CENTER);

        row.getCell(0).setCellStyle(centeredBoldCellStyle);
        row.getCell(6).setCellStyle(centeredBoldCellStyle);
        row.getCell(7).setCellStyle(centeredBoldCellStyle);
    }
}
