package pakoswdt.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import pakoswdt.MainApp;
import pakoswdt.model.AlertEnum;
import pakoswdt.model.Alerts;
import pakoswdt.model.Data;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SetInvoicePathDialogController {
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
        path.setText(Data.getDefaultInvoicePath());
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
            Data.setDefaultInvoicePath(path.textProperty().get());
            mainApp.saveData();
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid(String path) {
        if ( StringUtils.isNoneBlank(path) ) return true;
        else {
            setPath();
            new Alerts(AlertEnum.NOT_FILLED_FIELDS, mainApp.getPrimaryStage()).display();
            return false;
        }
    }

    private boolean isPathValid(String path) {
        if ( Files.exists(Paths.get(path)) ) return true;
        else {
            new Alerts(AlertEnum.INVALID_PATH, mainApp.getPrimaryStage()).display();
            return false;
        }
    }
}
