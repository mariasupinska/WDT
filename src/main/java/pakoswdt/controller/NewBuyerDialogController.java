package pakoswdt.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import pakoswdt.model.Buyer;

public class NewBuyerDialogController {
    @FXML
    private TextField name;
    @FXML
    private TextField street;
    @FXML
    private TextField city;
    @FXML
    private TextField postalCode;
    @FXML
    private TextField country;
    @FXML
    private TextField nip;

    private Stage dialogStage;

    @Getter
    private Buyer buyer;

    @FXML
    private void initialize() {
        buyer = new Buyer("","","","","","");
        name.textProperty().bindBidirectional(buyer.getName());
        street.textProperty().bindBidirectional(buyer.getStreet());
        city.textProperty().bindBidirectional(buyer.getCity());
        postalCode.textProperty().bindBidirectional(buyer.getPostalCode());
        country.textProperty().bindBidirectional(buyer.getCountry());
        nip.textProperty().bindBidirectional(buyer.getNip());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleOK() {
        if (isInputValid(this.buyer)) {
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
        this.buyer = null;
        dialogStage.close();
    }

    private boolean isInputValid(Buyer buyer) {
        return StringUtils.isNoneBlank(buyer.getName().get(), buyer.getStreet().get(), buyer.getCity().get(), buyer.getPostalCode().get(), buyer.getCountry().get(), buyer.getNip().get());
    }
}