package pakoswdt.view;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pakoswdt.MainApp;
import pakoswdt.model.Buyer;
import pakoswdt.model.Data;
import pakoswdt.model.Vehicle;

import java.io.IOException;
import java.util.Comparator;
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
        
        cargoDeliveryDate.valueProperty().bindBidirectional(Data.getInvoice().getDeliveryDate());

        buyers.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null ) {
                        if ( oldValue != null ) {
                            unbindOldValues(oldValue);
                        }
                        Data.getInvoice().setBuyer(newValue);
                        
                        bindNewValues(newValue);
                    } else {
                        clearBuyerFields();
                    }
                } );

        vehicles.getSelectionModel().selectedItemProperty().addListener(
                (observableVehicle, oldValueVehicle, newValueVehicle) -> {
                    if (newValueVehicle != null ) {
                        Data.getInvoice().setTransport(newValueVehicle);
                    }
                } );

        buyers.setValue(Data.getInvoice().getBuyer());
        vehicles.setValue(Data.getInvoice().getTransport());
    }

    private void clearBuyerFields() {
        name.clear();
        street.clear();
        city.clear();
        postalCode.clear();
        country.clear();
        nip.clear();
        vehicles.setItems(new SimpleListProperty<>());

        deliveryStreet.clear();
        deliveryCity.clear();
        deliveryPostalCode.clear();
        deliveryCountry.clear();

        personRetrieving.clear();
        personConfirming.clear();
    }

    private void unbindOldValues(Buyer buyer) {
        name.textProperty().unbindBidirectional(buyer.getName());
        street.textProperty().unbindBidirectional(buyer.getStreet());
        city.textProperty().unbindBidirectional(buyer.getCity());
        postalCode.textProperty().unbindBidirectional(buyer.getPostalCode());
        country.textProperty().unbindBidirectional(buyer.getCountry());
        nip.textProperty().unbindBidirectional(buyer.getNip());
        vehicles.itemsProperty().unbindBidirectional(buyer.getVehicles());

        deliveryStreet.textProperty().unbindBidirectional(buyer.getDeliveryStreet());
        deliveryCity.textProperty().unbindBidirectional(buyer.getDeliveryCity());
        deliveryPostalCode.textProperty().unbindBidirectional(buyer.getDeliveryPostalCode());
        deliveryCountry.textProperty().unbindBidirectional(buyer.getDeliveryCountry());

        personRetrieving.textProperty().unbindBidirectional(buyer.getPersonRetrieving());
        personConfirming.textProperty().unbindBidirectional(buyer.getPersonConfirming());
    }

    private void bindNewValues(Buyer buyer) {
        name.textProperty().bindBidirectional(buyer.getName());
        street.textProperty().bindBidirectional(buyer.getStreet());
        city.textProperty().bindBidirectional(buyer.getCity());
        postalCode.textProperty().bindBidirectional(buyer.getPostalCode());
        country.textProperty().bindBidirectional(buyer.getCountry());
        nip.textProperty().bindBidirectional(buyer.getNip());
        vehicles.itemsProperty().bindBidirectional(buyer.getVehicles());

        deliveryStreet.textProperty().bindBidirectional(buyer.getDeliveryStreet());
        deliveryCity.textProperty().bindBidirectional(buyer.getDeliveryCity());
        deliveryPostalCode.textProperty().bindBidirectional(buyer.getDeliveryPostalCode());
        deliveryCountry.textProperty().bindBidirectional(buyer.getDeliveryCountry());

        personRetrieving.textProperty().bindBidirectional(buyer.getPersonRetrieving());
        personConfirming.textProperty().bindBidirectional(buyer.getPersonConfirming());
    }

    @FXML
    private void handleAddCompany() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/NewBuyerDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Nowa firma");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(mainApp.getPrimaryStage());
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        NewBuyerDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.showAndWait();

        Buyer buyer = controller.getBuyer();
        if ( buyer != null ) {
            Data.getBuyers().add(buyer);
            FXCollections.sort(Data.getBuyers());
            buyers.getSelectionModel().select(buyer);
        }

    }

    @FXML
    private void handleDeleteCompany() {
        int selectedIndex = buyers.getSelectionModel().getSelectedIndex();
        if ( selectedIndex >= 0 ) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setHeaderText("Usuwanie firmy");
            alert.setContentText("Czy na pewno chcesz usunąć wybraną firmę?");
            Optional<ButtonType> result = alert.showAndWait();
            if( result.get() == ButtonType.OK ) {
                Buyer removed = buyers.getItems().remove(selectedIndex);
                buyers.setValue(null);
                unbindOldValues(removed);
                Data.getInvoice().setBuyer(null);
            } else if ( result.get() == ButtonType.CANCEL ) {
                return;
            }

        } else {
            //Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Brak zaznaczenia");
            alert.setHeaderText("Nie wybrano żadnej firmy");
            alert.setContentText("Aby usunąć firmę należy ją najpierw wybrać z listy.");
            alert.showAndWait();
        }
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
