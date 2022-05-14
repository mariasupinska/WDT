package pakoswdt.file;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import pakoswdt.model.InvoiceSummary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ExcelReportWriter {
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private Window window;
    private String defaultFileExtension = ".xls";
    private String templateFile = "template.xls";

    public ExcelReportWriter(Window window){
        this.workbook = new HSSFWorkbook();
        this.window = window;
    }

    public void createExcel(List<InvoiceSummary> invoiceSummaries){
        Optional<HSSFWorkbook> optWorkbook = openTemplate(new File(templateFile));
        if (optWorkbook.isPresent()) {
            this.workbook = optWorkbook.get();
            this.sheet = workbook.getSheet("Raport");
        } else {
        }

        int rownum = 4;
        int beginning = rownum;

        for (InvoiceSummary invSum : invoiceSummaries) {
            setCellsWithInvSum(invSum, rownum);
            rownum++;
        }

        setCellWithFooter(beginning, rownum);
        save();
    }

    public Optional<HSSFWorkbook> openTemplate(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Optional <HSSFWorkbook> optWorkbook = Optional.empty();
        try {
            optWorkbook = Optional.of(new HSSFWorkbook(fis));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optWorkbook;
    }

    private void save() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("XLS, ODS", "*.xls", "*.ods"));
            File file = fileChooser.showSaveDialog(window);
            if(file == null) {
                return;
            }

            if (!file.getName().endsWith(".xls") && !file.getName().endsWith(".ods")) {
                String name = file.getName();
                file = new File(name + this.defaultFileExtension);
            }

            FileOutputStream out = new FileOutputStream(file);
            this.workbook.write(out);
            out.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int setCellsWithInvSum(InvoiceSummary invoiceSummary, int rownum) {
        CellStyle centeredCellStyle = new CellStyleBuilder(workbook).align(CellStyle.ALIGN_CENTER).build();
        CellStyle rightAlignedCellStyle = new CellStyleBuilder(workbook).align(CellStyle.ALIGN_RIGHT).dataFormat("0.00").build();
        Row row = this.sheet.createRow(rownum);

        row.createCell(0).setCellValue(rownum - 3);
        row.getCell(0).setCellStyle(centeredCellStyle);

        String date = invoiceSummary.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        row.createCell(1).setCellValue(date);
        row.getCell(1).setCellStyle(centeredCellStyle);

        row.createCell(2).setCellValue(invoiceSummary.getInvoiceNumber());
        row.getCell(2).setCellStyle(centeredCellStyle);

        row.createCell(3).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        row.getCell(3).setCellValue(invoiceSummary.getPackagesWeight().doubleValue());
        row.getCell(3).setCellStyle(rightAlignedCellStyle);

        row.createCell(4).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        row.getCell(4).setCellValue(invoiceSummary.getPaperWeight().doubleValue());
        row.getCell(4).setCellStyle(rightAlignedCellStyle);

        row.createCell(5).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        row.getCell(5).setCellValue(invoiceSummary.getFoilWeight().doubleValue());
        row.getCell(5).setCellStyle(rightAlignedCellStyle);

        row.createCell(6).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        row.getCell(6).setCellValue(invoiceSummary.getPalettesWeight().doubleValue());
        row.getCell(6).setCellStyle(rightAlignedCellStyle);

        return rownum;
    }

    private int setCellWithFooter(int beginning, int rownum) {
        CellStyle centeredCellStyle = new CellStyleBuilder(workbook).align(CellStyle.ALIGN_CENTER).build();
        CellStyle rightAlignedCellStyle = new CellStyleBuilder(workbook).align(CellStyle.ALIGN_RIGHT).build();

        Row row = this.sheet.createRow(rownum + 1);

        row.createCell(2).setCellValue("RAZEM:");
        row.getCell(2).setCellStyle(centeredCellStyle);

        String formulaD= "SUM(D" + beginning + ":D" + rownum + ")";
        row.createCell(3).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        row.getCell(3).setCellFormula(formulaD);
        row.getCell(3).setCellStyle(rightAlignedCellStyle);

        String formulaE= "SUM(E" + beginning + ":E" + rownum + ")";
        row.createCell(4).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        row.getCell(4).setCellFormula(formulaE);
        row.getCell(4).setCellStyle(rightAlignedCellStyle);

        String formulaF= "SUM(F" + beginning + ":F" + rownum + ")";
        row.createCell(5).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        row.getCell(5).setCellFormula(formulaF);
        row.getCell(5).setCellStyle(rightAlignedCellStyle);

        String formulaG= "SUM(G" + beginning + ":G" + rownum + ")";
        row.createCell(6).setCellType(HSSFCell.CELL_TYPE_FORMULA);
        row.getCell(6).setCellFormula(formulaG);
        row.getCell(6).setCellStyle(rightAlignedCellStyle);

        return rownum;
    }
}
