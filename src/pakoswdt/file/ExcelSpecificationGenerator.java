package pakoswdt.file;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import pakoswdt.model.Invoice;
import pakoswdt.model.Package;
import pakoswdt.model.Product;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class ExcelSpecificationGenerator extends ExcelCommonMethods {
    public ExcelSpecificationGenerator(HSSFWorkbook hssfWorkbook, Invoice invoice, List<Product> products) {
        super(hssfWorkbook, invoice, products);
    }

    public void createSpecification(Sheet sheet) {
        setPrintSetup(sheet);
        setHeaderText(sheet, "SPECYFIKACJA POSZCZEGÓLNYCH SZTUK ŁADUNKU");
        setInvoiceText(sheet);
        setBusinessText(sheet);
        setTableLegend(sheet);
        setTableData(sheet);
        setSummaryData(sheet);
        setSpecificationFooterText(sheet);
        setAutosizeColumns(sheet);
    }

    private void setTableLegend(Sheet sheet) {
        Font font = createStandardBoldFont();

        CellStyle cellStyle = hssfWorkbook.createCellStyle();
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle = createBorderedCellStyle(cellStyle);

        sheet.addMergedRegion(new CellRangeAddress(9, 9, 1, 5));

        Row row = sheet.createRow(9);
        row.setHeightInPoints(sheet.getDefaultRowHeightInPoints() * 2);

        row.createCell(0).setCellValue("L.p. ");
        row.createCell(1).setCellValue("Nazwa");
        row.createCell(6).setCellValue("Ilość \njedn.");
        row.createCell(7).setCellValue("J.m.");
        row.createCell(8).setCellValue("Waga \nnetto");
        row.createCell(9).setCellValue("Rodzaj \nopak.");
        row.createCell(10).setCellValue("Waga \nopak.");
        row.createCell(11).setCellValue("Ilość \nopak.");

        for (Cell c : row) {
            c.setCellStyle(cellStyle);
        }
    }

    private void setTableData(Sheet sheet) {
        Collections.sort(products, productPackageComparator);

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductPackage().isMultiPackage()) {
                i += putMultiPackage(sheet, i);
            } else {
                putNormalPackage(sheet, i);
            }
        }
    }

    private void setSummaryData(Sheet sheet) { //TODO: PRZED WYGENEROWANIEM TRZEBA NAJPIERW OBLICZYĆ SUMMARY
        CellStyle cellStyle = createCenteredCellStyle().bold(true).build();
        CellStyle cellStyleTopBorder = createCenteredCellStyle().borderTop().build();
        CellStyle cellStyleBordered = createCenteredCellStyle().bold(true).borderAll().build();

        Row row = sheet.createRow(sheet.getLastRowNum() + 1);

        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 6, 7));

        row.createCell(6).setCellValue("Razem: ");
        row.createCell(8).setCellValue(invoice.getSummary().getNetWeight().setScale(2, BigDecimal.ROUND_HALF_UP).toString().replace(".", ",") + " [kg]");
        row.createCell(9).setCellStyle(cellStyleTopBorder);
        row.createCell(10).setCellValue(invoice.getSummary().getPackagesWeight().setScale(2, BigDecimal.ROUND_HALF_UP).toString().replace(".", ",") + " [kg]");
        row.createCell(11).setCellValue(invoice.getSummary().getPackagesAmount().setScale(0, BigDecimal.ROUND_HALF_UP).toString());

        for (Cell c : row) {
            if (c.getColumnIndex() != 9) {
                c.setCellStyle(cellStyleBordered);
            }
        }

        row = sheet.createRow(sheet.getLastRowNum() + 2);

        for (int i = 0; i < 4; i++) {
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum() + i, row.getRowNum() + i, 1, 3));
        }
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 8, 11));

        row.createCell(8).setCellValue("Waga brutto: " + invoice.getSummary().getGrossWeight().setScale(2, BigDecimal.ROUND_HALF_UP).toString().replace(".", ",") + " [kg]");
        row.getCell(8).setCellStyle(cellStyle);

        row.createCell(1).setCellValue("OPAKOWANIA");
        row.getCell(1).setCellStyle(cellStyle);

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(1).setCellValue("Papier, karton: " + invoice.getSummary().getPaperWeight().setScale(2, BigDecimal.ROUND_HALF_UP).toString().replace(".", ",") + " [kg]");

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(1).setCellValue("Tworzywa sztuczne: " + invoice.getSummary().getFoilWeight().setScale(2, BigDecimal.ROUND_HALF_UP).toString().replace(".", ",") + " [kg]");

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(1).setCellValue("Palety: " + invoice.getSummary().getPalettesWeight().setScale(2, BigDecimal.ROUND_HALF_UP).toString().replace(".", ",") + " [kg]");
    }

    private void setSpecificationFooterText(Sheet sheet) {
        CellStyle cellStyle = createCenteredCellStyle().build();
        CellStyle borderedCenteredCellStyle = createCenteredCellStyle().bold(true).build();

        setSpecificationTransportData(sheet);

        Row row = sheet.createRow(sheet.getLastRowNum() + 2);

        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, 1, 5));
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 8, 11));
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum() + 3, row.getRowNum() + 3, 8, 11));
        row.createCell(1).setCellValue("Towar zgodnie ze specyfikacją odebrano: \n" +
                invoice.getPlaceOfExtradition().get() + ", dn. " +
                invoice.getCreationDate().get().toString());
        row.getCell(1).setCellStyle(cellStyle);
        row.createCell(8).setCellValue("Specyfikację sporządził: ");
        row.getCell(8).setCellStyle(cellStyle);

        //odbiorca i miejsce na podpis
        row = sheet.createRow(sheet.getLastRowNum() + 3);
        for (int i = 0; i < 3; i++) {
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum() + i, row.getRowNum() + i, 1, 5));
        }
        row.createCell(1).setCellValue(invoice.getBuyer().getPersonRetrieving().get());
        row.getCell(1).setCellStyle(borderedCenteredCellStyle);
        row.createCell(8).setCellValue(invoice.getCreator());
        row.getCell(8).setCellStyle(borderedCenteredCellStyle);

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(1).setCellValue("..........................................");
        row.getCell(1).setCellStyle(cellStyle);

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(1).setCellValue("/ signature & stamp /");

        CellStyle smallCellStyle = new CellStyleBuilder(hssfWorkbook).fontSize((short) 8).align(CellStyle.ALIGN_CENTER).build();

        row.getCell(1).setCellStyle(smallCellStyle);
    }

    private void setSpecificationTransportData(Sheet sheet) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 2);
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 3));
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 4, 6));

        row.createCell(1).setCellValue("Środek transportu: samochód ");
        row.getCell(1).setCellStyle(createRightAlignedCellStyle());

        row.createCell(4).setCellValue(invoice.getTransport().getBrand().get() + " ** " + invoice.getTransport().getNumber().get());
        row.getCell(4).setCellStyle(createLeftAlignedBoldCellStyle().build());
    }

    private int putMultiPackage(Sheet sheet, int rowNumber) {
        Product product = products.get(rowNumber);
        Package productPackage = product.getProductPackage();

        CellStyle standardCellStyle = hssfWorkbook.createCellStyle();
        standardCellStyle = createBorderedCellStyle(standardCellStyle);
        standardCellStyle.setAlignment(CellStyle.ALIGN_CENTER);

        CellStyle centeredBorderedCellStyle = createCenteredCellStyle().borderAll().build();

        CellStyle rightAlignedStyle = createBorderedCellStyle(hssfWorkbook.createCellStyle());
        rightAlignedStyle.setAlignment(CellStyle.ALIGN_RIGHT);

        int iterator = 0;

        while (product.getProductPackage().equals(productPackage)) {
            sheet.addMergedRegion(new CellRangeAddress(rowNumber + 10 + iterator, rowNumber + 10 + iterator, 1, 5));

            Row row = sheet.createRow(rowNumber + 10 + iterator);
            row.createCell(0).setCellValue(rowNumber + iterator + 1);
            row.createCell(1).setCellValue(product.getName().get());
            row.createCell(6).setCellValue(BigDecimal.valueOf(product.getAmount().get()).setScale(2, BigDecimal.ROUND_HALF_UP).toString().replace(".", ","));
            row.createCell(7).setCellValue(product.getUnit().get());
            row.createCell(8).setCellValue(BigDecimal.valueOf(product.getNetWeight().get()).setScale(2, BigDecimal.ROUND_HALF_UP).toString().replace(".", ","));
            row.getCell(0).setCellStyle(centeredBorderedCellStyle);
            row.getCell(1).setCellStyle(standardCellStyle);
            row.getCell(1).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            row.getCell(6).setCellStyle(rightAlignedStyle);
            row.getCell(7).setCellStyle(centeredBorderedCellStyle);
            row.getCell(8).setCellStyle(rightAlignedStyle);

            if (rowNumber + iterator + 1 < products.size()
                    && products.get(rowNumber + iterator + 1).getProductPackage().equals(productPackage)) {
                iterator++;
                product = products.get(rowNumber + iterator);
            } else
                break;
        }

        sheet.addMergedRegion(new CellRangeAddress(rowNumber + 10, rowNumber + 10 + iterator, 9, 9));
        sheet.addMergedRegion(new CellRangeAddress(rowNumber + 10, rowNumber + 10 + iterator, 10, 10));
        sheet.addMergedRegion(new CellRangeAddress(rowNumber + 10, rowNumber + 10 + iterator, 11, 11));

        sheet.getRow(rowNumber + 10).createCell(9).setCellValue(productPackage.getType().get().replaceAll("\\d+.*", ""));


        BigDecimal totalWeight = BigDecimal.valueOf(product.getProductPackage().getWeight().doubleValue())
                .multiply(BigDecimal.valueOf(product.getProductPackage().getAmount().doubleValue()));

        sheet.getRow(rowNumber + 10).createCell(10).setCellValue(totalWeight.setScale(2, BigDecimal.ROUND_HALF_UP).toString().replace(".", ","));
        sheet.getRow(rowNumber + 10).createCell(11).setCellValue(BigDecimal.valueOf(productPackage.getAmount().get()).setScale(0, BigDecimal.ROUND_HALF_UP).toString());

        sheet.getRow(rowNumber + 10).getCell(9).setCellStyle(centeredBorderedCellStyle);
        sheet.getRow(rowNumber + 10).getCell(10).setCellStyle(centeredBorderedCellStyle);
        sheet.getRow(rowNumber + 10).getCell(11).setCellStyle(centeredBorderedCellStyle);

        return iterator;
    }

    private void putNormalPackage(Sheet sheet, int rowNumber) {
        Product product = products.get(rowNumber);

        CellStyle standardCellStyle = new CellStyleBuilder(hssfWorkbook).borderAll().build();
        CellStyle leftAlignedCellStyle = new CellStyleBuilder(hssfWorkbook).borderAll().align(CellStyle.ALIGN_LEFT).build();
        CellStyle rightAlignedStyle = new CellStyleBuilder(hssfWorkbook).borderAll().align(CellStyle.ALIGN_RIGHT).build();
        CellStyle centeredCellStyle = createCenteredCellStyle().borderAll().build();

        sheet.addMergedRegion(new CellRangeAddress(rowNumber + 10, rowNumber + 10, 1, 5));

        Row row = sheet.createRow(rowNumber + 10);
        row.createCell(0).setCellValue(rowNumber + 1);
        row.createCell(1).setCellValue(product.getName().get());
        row.createCell(6).setCellValue(BigDecimal.valueOf(product.getAmount().get()).setScale(2, BigDecimal.ROUND_HALF_UP).toString().replace(".", ","));
        row.createCell(7).setCellValue(product.getUnit().get());
        row.createCell(8).setCellValue(BigDecimal.valueOf(product.getNetWeight().get()).setScale(2, BigDecimal.ROUND_HALF_UP).toString().replace(".", ","));
        row.createCell(9).setCellValue(product.getProductPackage().getType().get().replaceAll("\\d+.*", ""));
        BigDecimal totalWeight = BigDecimal.valueOf(product.getProductPackage().getWeight().doubleValue())
                .multiply(BigDecimal.valueOf(product.getProductPackage().getAmount().doubleValue()));
        row.createCell(10).setCellValue(totalWeight.setScale(2, BigDecimal.ROUND_HALF_UP).toString().replace(".", ","));
        row.createCell(11).setCellValue(BigDecimal.valueOf(product.getAmount().get()).setScale(0, BigDecimal.ROUND_HALF_UP).toString());

        sheet.getRow(rowNumber + 10).getCell(0).setCellStyle(centeredCellStyle);
        sheet.getRow(rowNumber + 10).getCell(1).setCellStyle(leftAlignedCellStyle);
        sheet.getRow(rowNumber + 10).getCell(6).setCellStyle(rightAlignedStyle);
        sheet.getRow(rowNumber + 10).getCell(7).setCellStyle(centeredCellStyle);
        sheet.getRow(rowNumber + 10).getCell(8).setCellStyle(rightAlignedStyle);
        sheet.getRow(rowNumber + 10).getCell(9).setCellStyle(centeredCellStyle);
        sheet.getRow(rowNumber + 10).getCell(10).setCellStyle(centeredCellStyle);

        CellStyle integerCellStyle = createCenteredCellStyle().dataFormat("#,##0").borderAll().build();
        sheet.getRow(rowNumber + 10).getCell(11).setCellStyle(integerCellStyle);
    }
}
