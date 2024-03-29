package pakoswdt.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import pakoswdt.MainApp;
import pakoswdt.model.AlertEnum;
import pakoswdt.model.Alerts;
import pakoswdt.model.Data;

@Slf4j
public class StartingViewController {
    private MainApp mainApp;

    public StartingViewController() {

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleNewWDT() {
        Data.createNewWDT();
        mainApp.showSellerOverview();
    }

    @FXML
    private void handleGenerateYearlyReport() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/GenerateReportDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (Exception e) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, mainApp.getPrimaryStage()).display();
            log.error("Unhandled exception", e);
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Generowanie zestawienia rocznego WDT");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(mainApp.getPrimaryStage());
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        GenerateReportDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMainApp(mainApp);
        dialogStage.showAndWait();
    }

    @FXML
    private void handleSetDefaultSavePath() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/SetInvoiceSummaryPathDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (Exception e) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, mainApp.getPrimaryStage()).display();
            log.error("Unhandled exception", e);
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Ścieżka do zapisu dokumentów");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(mainApp.getPrimaryStage());
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        SetInvoiceSummaryPathDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMainApp(mainApp);
        controller.setPath();
        dialogStage.showAndWait();
    }

    @FXML
    private void handleSetDefaultInvoiceSavePath() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/SetInvoicePathDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (Exception e) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, mainApp.getPrimaryStage()).display();
            log.error("Unhandled exception", e);
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Ścieżka do zapisu faktur");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(mainApp.getPrimaryStage());
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        SetInvoicePathDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMainApp(mainApp);
        controller.setPath();
        dialogStage.showAndWait();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
