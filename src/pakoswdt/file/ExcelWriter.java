package pakoswdt.file;

import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import pakoswdt.MainApp;
import pakoswdt.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Slf4j
public class ExcelWriter {
    private MainApp mainApp;
    private HSSFWorkbook hssfWorkbook;
    private Invoice invoice;
    private List<Product> products;

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

        String defaultSavePath = Data.getDefaultInvoicePath();
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
                hssfWorkbook.write(fileOut);
                fileOut.flush();
                fileOut.close();

                saveInvoiceSummaryFile();

                new Alerts(AlertEnum.SUCCESSFUL_INVOICE_AND_SUMMARY_GENERATION, mainApp.getPrimaryStage()).display();
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveInvoiceSummaryFile() {
        JsonWriter jsonWriter = new JsonWriter();
        jsonWriter.exportInvoiceSummary(Data.getInvoice().getNumber().get(), Data.getInvoice().getSummary(), Data.getDefaultInvoiceSummaryPath());
    }
}
