package pakoswdt.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import pakoswdt.MainApp;
import pakoswdt.model.Data;


public class SellerOverviewController {
    private MainApp mainApp;

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

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        name.textProperty().bindBidirectional(Data.seller.getName());
        street.textProperty().bindBidirectional(Data.seller.getStreet());
        city.textProperty().bindBidirectional(Data.seller.getCity());
        postalCode.textProperty().bindBidirectional(Data.seller.getPostalCode());
        country.textProperty().bindBidirectional(Data.seller.getCountry());
        nip.textProperty().bindBidirectional(Data.seller.getNip());
    }

    @FXML
    private void handleNext() {
        mainApp.showBuyerOverview();
    }

    @FXML
    private void handlePrevious() {
        mainApp.showStartingView();
    }
}
