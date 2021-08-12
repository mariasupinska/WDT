package pakoswdt.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import pakoswdt.MainApp;
import pakoswdt.model.Buyer;
import pakoswdt.model.Data;
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
        buyers.setItems(Data.getBuyers());

        buyers.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue != null ) {
                        if ( oldValue != null ) {
                            name.textProperty().unbindBidirectional(Data.getBuyer().getName());
                            street.textProperty().unbindBidirectional(Data.getBuyer().getStreet());
                            city.textProperty().unbindBidirectional(Data.getBuyer().getCity());
                            postalCode.textProperty().unbindBidirectional(Data.getBuyer().getPostalCode());
                            country.textProperty().unbindBidirectional(Data.getBuyer().getCountry());
                            nip.textProperty().unbindBidirectional(Data.getBuyer().getNip());
                            cargoDeliveryDate.valueProperty().unbindBidirectional(Data.getInvoice().getDeliveryDate());
                            vehicles.itemsProperty().unbindBidirectional(Data.getBuyer().getVehicles());
                        }

                        Data.getInvoice().setBuyer(newValue);
                        name.textProperty().bindBidirectional(Data.getBuyer().getName());
                        street.textProperty().bindBidirectional(Data.getBuyer().getStreet());
                        city.textProperty().bindBidirectional(Data.getBuyer().getCity());
                        postalCode.textProperty().bindBidirectional(Data.getBuyer().getPostalCode());
                        country.textProperty().bindBidirectional(Data.getBuyer().getCountry());
                        nip.textProperty().bindBidirectional(Data.getBuyer().getNip());
                        cargoDeliveryDate.valueProperty().bindBidirectional(Data.getInvoice().getDeliveryDate());
                        vehicles.itemsProperty().bindBidirectional(Data.getBuyer().getVehicles());

                        vehicles.getSelectionModel().selectedItemProperty().addListener(
                                (observable1, oldValue1, newValue1) -> {
                                    if(newValue1 != null ) {
                                        Data.getInvoice().setTransport(newValue1);
                                    }
                                } );
                    }
                } );

        buyers.setValue(Data.getInvoice().getBuyer());
        vehicles.setValue(Data.getInvoice().getTransport());
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
