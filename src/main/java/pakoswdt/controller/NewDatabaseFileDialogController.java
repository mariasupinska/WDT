package pakoswdt.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pakoswdt.MainApp;
import pakoswdt.file.DataFile;
import pakoswdt.model.AlertEnum;
import pakoswdt.model.Alerts;
import pakoswdt.model.Data;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class NewDatabaseFileDialogController {
    @FXML
    private TextField path;

    private Stage dialogStage;

    private MainApp mainApp;

    @FXML
    private void initialize() {
    }

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
    private void handleSetNewDatabaseFilePath() {
        try {
            FileChooser fileChooser = new FileChooser();

            File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

            if (file == null || StringUtils.isBlank(file.getName())) {
                new Alerts(AlertEnum.INCORRECT_FILE_NAME, mainApp.getPrimaryStage()).display();
                return;
            }

            if (!file.getName().endsWith(".json")) {
                String name = file.getName();
                String path = file.getPath().replace(name, "");
                file = new File(path + name + ".json");
            }

            if (isPathValid(file)) {
                path.setText(file.getAbsolutePath());
            }

        } catch (Exception ex) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, mainApp.getPrimaryStage()).display();
            log.error("Unhandled exception: ", ex);
        }
    }

    @FXML
    private void handleOK() {
        if (isInputValid(path.textProperty().get())) {
            DataFile.setDataFilePath(path.textProperty().get());
            Data.setDefaultDatabasePath(path.textProperty().get());
            mainApp.saveData();
            dialogStage.close();
        }
    }

    private boolean isInputValid(String path) {
        if (StringUtils.isNoneBlank(path)) return true;
        else {
            setPath();
            new Alerts(AlertEnum.NOT_FILLED_FIELDS, mainApp.getPrimaryStage()).display();
            return false;
        }
    }

    private boolean isPathValid(File file) {
        String name = file.getName();
        String path = file.getPath().replace(name, "");
        if (Files.exists(Paths.get(path))) return true;
        else {
            new Alerts(AlertEnum.INVALID_PATH, mainApp.getPrimaryStage()).display();
            return false;
        }
    }
}
