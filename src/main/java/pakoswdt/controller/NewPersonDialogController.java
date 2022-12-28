package pakoswdt.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public class NewPersonDialogController {
    @FXML
    private TextField name;

    private Stage dialogStage;

    @Getter private String person;

    @FXML
    private void initialize() {

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleOK() {
        if (isInputValid(name.textProperty().get())) {
            this.person = name.textProperty().get();
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
        this.person = null;
        dialogStage.close();
    }

    private boolean isInputValid(String person) {
        return StringUtils.isNoneBlank(person);
    }
}
