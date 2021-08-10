package pakoswdt.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import pakoswdt.model.Vehicle;

public class NewVehicleDialogController {
    @FXML
    private TextField brand;
    @FXML
    private TextField number;

    private Stage dialogStage;

    @Getter private Vehicle vehicle;

    @FXML
    private void initialize() {
        vehicle = new Vehicle("", "");
        brand.textProperty().bindBidirectional(vehicle.getBrand());
        number.textProperty().bindBidirectional(vehicle.getNumber());
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleOK() {
        if (isInputValid(this.vehicle)) {
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
        this.vehicle = null;
        dialogStage.close();
    }
    private boolean isInputValid(Vehicle vehicle) {
        return StringUtils.isNoneBlank(vehicle.getBrand().get(), vehicle.getNumber().get());
    }
}
