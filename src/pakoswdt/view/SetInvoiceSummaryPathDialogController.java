package pakoswdt.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import pakoswdt.MainApp;
import pakoswdt.model.Data;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SetInvoiceSummaryPathDialogController {
    @FXML
    private TextField path;

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

    public void setPath() {
        path.setText(Data.getDefaultInvoiceSummaryPath());
    }

    @FXML
    private void handleChangeDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(path.getScene().getWindow());
        try {
            if (isPathValid(selectedDirectory.getAbsolutePath())) {
                path.setText(selectedDirectory.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOK() {
        if (isInputValid(path.textProperty().get()) && isPathValid(path.textProperty().get())) {
            Data.setDefaultInvoiceSummaryPath(path.textProperty().get());
            mainApp.saveData();
            dialogStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(dialogStage);
            alert.setTitle("Brak danych");
            alert.setHeaderText("Nie wprowadzono prawidłowych danych");
            alert.setContentText("Proszę wypełnić wszystkie pola.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid(String path) {
        return StringUtils.isNoneBlank(path);
    }

    private boolean isPathValid(String path) {
        return Files.exists(Paths.get(path));
    }
}
