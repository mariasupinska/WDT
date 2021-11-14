package pakoswdt.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pakoswdt.MainApp;
import pakoswdt.model.Data;

import java.io.IOException;

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

    }

    @FXML
    private void handleSetDefaultSavePath() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/SetInvoiceSummaryPathDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
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
    private void handleExit() {
        System.exit(0);
    }
}
