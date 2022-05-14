package pakoswdt.view;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.lang3.StringUtils;
import pakoswdt.MainApp;
import pakoswdt.file.ExcelReportWriter;
import pakoswdt.file.JsonReader;
import pakoswdt.model.AlertEnum;
import pakoswdt.model.Alerts;
import pakoswdt.model.Data;
import pakoswdt.model.InvoiceSummary;

import java.io.File;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateReportDialogController {
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;

    private Stage dialogStage;

    private MainApp mainApp;

    @FXML
    private void initialize() {}

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleOK() {
        if (isInputValid() && inCorrectOrder()) {
            generateReport(dateFrom.getValue(), dateTo.getValue());
            dialogStage.close();
        }
    }

    private void generateReport(LocalDate from, LocalDate to) {
        Window window = dateFrom.getScene().getWindow();

        String[] extension = new String[]{"json"};
        JsonReader jsonReader = new JsonReader();

        if (StringUtils.isEmpty(Data.getDefaultInvoiceSummaryPath())) {
            new Alerts(AlertEnum.INVALID_PATH, mainApp.getPrimaryStage()).display();
            return;
        }

        List<InvoiceSummary> summaries = jsonReader.getListOfInvoiceSummariesFrom(new File(Data.getDefaultInvoiceSummaryPath()), extension)
                .stream()
                .filter(summary -> isAfterOrEqual(summary.getCreationDate(), from) && isBeforeOrEqual(summary.getCreationDate(), to))
                .sorted(Comparator.comparing(InvoiceSummary::getInvoiceNumber))
                .collect(Collectors.toList());

        new ExcelReportWriter(window).createExcel(summaries);
    }

    private boolean isBeforeOrEqual(LocalDate compared, LocalDate comparator) {
        return compared.isBefore(comparator) || compared.isEqual(comparator);
    }

    private boolean isAfterOrEqual(LocalDate compared, LocalDate comparator) {
        return compared.isAfter(comparator) || compared.isEqual(comparator);
    }

    private boolean isInputValid() {
        if (dateFrom.getValue() != null && dateTo.getValue() != null) return true;
        else {
            new Alerts(AlertEnum.NOT_FILLED_FIELDS, mainApp.getPrimaryStage()).display();
            return false;
        }
    }

    private boolean inCorrectOrder() {
        if ( dateFrom.getValue().isBefore(dateTo.getValue()) ) return true;
        else {
            new Alerts(AlertEnum.DATES_IN_INCORRECT_ORDER, mainApp.getPrimaryStage()).display();
            return false;
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

}
