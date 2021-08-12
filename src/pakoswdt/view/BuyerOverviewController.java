package pakoswdt.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pakoswdt.MainApp;
import pakoswdt.model.Buyer;
import pakoswdt.model.Data;
import pakoswdt.model.Vehicle;

import java.io.IOException;
import java.util.Optional;

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
                    if (newValue != null ) {
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
                                (observableVehicle, oldValueVehicle, newValueVehicle) -> {
                                    if (newValueVehicle != null ) {
                                        Data.getInvoice().setTransport(newValueVehicle);
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/NewVehicleDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Nowy pojazd");
        dialogStage.initOwner(mainApp.getPrimaryStage());
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        NewVehicleDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.showAndWait();

        Vehicle vehicle = controller.getVehicle();
        if ( vehicle != null ) {
            Data.getBuyer().getVehicles().add(vehicle);
            FXCollections.sort(Data.getBuyer().getVehicles());
            vehicles.getSelectionModel().select(vehicle);
        }
    }

    @FXML
    private void handleDeleteVehicle() {
        int selectedIndex = vehicles.getSelectionModel().getSelectedIndex();
        if ( selectedIndex >= 0 ) {
            Vehicle vehicle = vehicles.getItems().get(selectedIndex);
            if ( vehicle.isEmpty() ) return;                                //TODO: przed przejściem do produktu czy oba vehicle nie są puste

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setHeaderText("Usuwanie pojazdu");
            alert.setContentText("Czy na pewno chcesz usunąć wybrany pojazd?");
            Optional<ButtonType> result = alert.showAndWait();
            if( result.get() == ButtonType.OK ) {
                Data.setTransport(null);
                vehicles.getItems().remove(selectedIndex);
            } else if ( result.get() == ButtonType.CANCEL ) {
                return;
            }

        } else {
            //Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Brak zaznaczenia");
            alert.setHeaderText("Nie wybrano żadnego pojazdu");
            alert.setContentText("Aby usunąć pojazd należy go najpierw wybrać z listy.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNext() {}

    @FXML
    private void handlePrevious() {
        mainApp.showSellerOverview();
    }
}
