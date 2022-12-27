package pakoswdt.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import pakoswdt.MainApp;
import pakoswdt.model.AlertEnum;
import pakoswdt.model.Alerts;
import pakoswdt.model.Data;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OldDatabaseFileDialogController {
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
        path.setText(Data.getDefaultDatabasePath());
    }

    @FXML
    private void readJsonFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter jsonExtFilter = new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(jsonExtFilter);
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if ( file != null && file.exists() ) {
            Data.setDefaultDatabasePath(file.getPath());
            path.setText(file.getAbsolutePath());
        } else {
            new Alerts(AlertEnum.CANNOT_LOAD_FILE, mainApp.getPrimaryStage()).display();
        }
    }

    @FXML
    private void handleOK() {
        if (isInputValid(path.textProperty().get()) && isPathValid(path.textProperty().get())) {
            //Data.setDefaultInvoicePath(path.textProperty().get());
            //mainApp.saveData();
            mainApp.setOldDatabaseFilePath(path.textProperty().get());
            dialogStage.close();
        }
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
