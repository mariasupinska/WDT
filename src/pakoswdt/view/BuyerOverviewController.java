package pakoswdt.view;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import pakoswdt.MainApp;
import pakoswdt.model.Buyer;
import pakoswdt.model.Vehicle;

public class BuyerOverviewController {
    private MainApp mainApp;

    @FXML
    private ChoiceBox<Buyer> buyers;

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
    @FXML
    private DatePicker cargoDeliveryDate;
    @FXML
    private ChoiceBox<Vehicle> vehicles;

    @FXML
    private TextField deliveryStreet;
    @FXML
    private TextField deliveryCity;
    @FXML
    private TextField deliveryPostalCode;
    @FXML
    private TextField deliveryCountry;
    @FXML
    private TextField personRetrieving;
    @FXML
    private TextField personConfirming;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    private void handleAddCompany() {

    }

    @FXML
    private void handleDeleteCompany() {

    }

    @FXML
    private void handleAddVehicle() {

    }

    @FXML
    private void handleDeleteVehicle() {

    }

    @FXML
    private void handleNext() {}

    @FXML
    private void handlePrevious() {
        mainApp.showSellerOverview();
    }
}
