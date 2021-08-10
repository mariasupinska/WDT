package pakoswdt.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import pakoswdt.MainApp;
import pakoswdt.model.Data;

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
    private void handleEditWDT() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("No Edit Feature");
        alert.setHeaderText("Not Possible Yet");
        alert.setContentText("Please be patient.");

        alert.showAndWait();
    }
    @FXML //AM I RIGHT
    private void handleExit() {
        System.exit(0);
    }
}
