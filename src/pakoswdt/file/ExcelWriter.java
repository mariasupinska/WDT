package pakoswdt.file;

import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import pakoswdt.MainApp;
import pakoswdt.model.Invoice;
import pakoswdt.model.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Slf4j
public class ExcelWriter {
    private MainApp mainApp;
    private HSSFWorkbook hssfWorkbook;
    private Invoice invoice;
    private List<Product> products;

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

    public ExcelWriter(MainApp mainApp, Invoice invoice, List<Product> products) {
        this.invoice = invoice;
        this.products = products;
        this.mainApp = mainApp;
    }

    public void createExcelFile() {
        Workbook workbook = new HSSFWorkbook();
        this.hssfWorkbook = (HSSFWorkbook) workbook;

        Sheet sheet1 = workbook.createSheet("Specyfikacja");
        Sheet sheet2 = workbook.createSheet("Potwierdzenie");

        new ExcelSpecificationGenerator(hssfWorkbook, invoice, products).createSpecification(sheet1);
        new ExcelConfirmationGenerator(hssfWorkbook, invoice, products).createConfirmation(sheet2);

        FileChooser fileChooser = new FileChooser();

        //TODO: WYBRANA ŚCIEŻKA DO ZAPISU DOKUMENTÓW
        String defaultSavePath = "target";
        if (StringUtils.isNotEmpty(defaultSavePath)) {
            fileChooser.setInitialDirectory(new File(defaultSavePath));
        }

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XLS files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);

        String systemSeparator = File.separator;
        if (systemSeparator.equals("\\")) {
            systemSeparator = "\\\\";
        }
        String fileName = invoice.getNumber().get().replaceAll(systemSeparator, "_");
        fileName = fileName.replaceAll("/", "_");

        fileChooser.setInitialFileName(fileName + ".xls");
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        saveExcelFile(file);
    }

    private void saveExcelFile(File file) {
        if (file != null) {
            if (!file.getPath().endsWith(".xls")) {
                file = new File(file.getPath() + ".xls");
            }
            FileOutputStream fileOut = null;
            try {
                fileOut = new FileOutputStream(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                hssfWorkbook.write(fileOut);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                fileOut.flush();
                fileOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
