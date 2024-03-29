package pakoswdt.controller;

import com.google.common.collect.Lists;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pakoswdt.MainApp;
import pakoswdt.model.*;

import java.util.Optional;

@Slf4j
public class BuyerOverviewController {
    private MainApp mainApp;

    @FXML
    private ComboBox<Buyer> buyers;

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

    @FXML
    private Button nextButton;

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
                        flushButton();
                    }
                } );

        vehicles.getSelectionModel().selectedItemProperty().addListener(
                (observableVehicle, oldValueVehicle, newValueVehicle) -> {
                    Data.getInvoice().setTransport(newValueVehicle);
                    flushButton();
                });

        buyers.setValue(Data.getInvoice().getBuyer());
        vehicles.setValue(Data.getInvoice().getTransport());

        Lists.newArrayList(name, street, city, postalCode, country, nip, deliveryStreet, deliveryCity, deliveryPostalCode, deliveryCountry).forEach(textField -> textField.textProperty().addListener((observable, newVal, oldVal) -> flushButton()));

        cargoDeliveryDate.valueProperty().addListener((observable, newVal, oldVal) -> flushButton());

        flushButton();
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

    private void flushButton() {
        if (isInputValid() && isTransportValid()) {
            this.nextButton.setStyle("-fx-background-color: #4dd922;\n" +
                    "    -fx-text-fill: black;\n");
        } else {
            this.nextButton.setStyle("");
        }
    }

    @FXML
    private void handleAddCompany() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/NewBuyerDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (Exception e) {
            log.error("Unhandled exception", e);
            new Alerts(AlertEnum.UNKNOWN_ERROR, mainApp.getPrimaryStage()).display();
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
            Optional<ButtonType> result = new Alerts(AlertEnum.DELETING_BUYER, mainApp.getPrimaryStage()).display();
            if( result.get() == ButtonType.OK ) {
                Buyer removed = buyers.getItems().remove(selectedIndex);
                buyers.setValue(null);
                unbindOldValues(removed);
                Data.getInvoice().setBuyer(null);
            } else if ( result.get() == ButtonType.CANCEL ) {
                return;
            }

        } else {
            new Alerts(AlertEnum.NO_BUYER_SELECTED, mainApp.getPrimaryStage()).display();
        }
    }

    @FXML
    private void handleAddVehicle() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/NewVehicleDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (Exception e) {
            log.error("Unhandled exception", e);
            new Alerts(AlertEnum.UNKNOWN_ERROR, mainApp.getPrimaryStage()).display();
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
            if ( vehicle.isEmpty() ) return;

            Optional<ButtonType> result = new Alerts(AlertEnum.DELETING_VEHICLE, mainApp.getPrimaryStage()).display();
            if( result.get() == ButtonType.OK ) {
                Data.setTransport(null);
                vehicles.getItems().remove(selectedIndex);
            }
        } else {
            new Alerts(AlertEnum.NO_VEHICLE_SELECTED, mainApp.getPrimaryStage()).display();
        }
    }

    @FXML
    private void handleNext() {
        if ( !isInputValid() ) {
            new Alerts(AlertEnum.NOT_FILLED_FIELDS, mainApp.getPrimaryStage()).display();
            return;
        }
        if ( !isTransportValid() ) {
            new Alerts(AlertEnum.VEHICLE_NOT_CHOSEN, mainApp.getPrimaryStage()).display();
            return;
        }
        mainApp.showProductsOverview();
    }

    private boolean isInputValid() {
        return StringUtils.isNoneBlank(name.textProperty().get(),
                street.textProperty().get(),
                city.textProperty().get(),
                postalCode.textProperty().get(),
                country.textProperty().get(),
                nip.textProperty().get(),
                deliveryStreet.textProperty().get(),
                deliveryCity.textProperty().get(),
                deliveryPostalCode.textProperty().get(),
                deliveryCountry.textProperty().get())
                && cargoDeliveryDate.valueProperty().get() != null;
    }

    private boolean isTransportValid() {
        return Data.getInvoice().getTransport() != null;
    }

    @FXML
    private void handlePrevious() {
        mainApp.showSellerOverview();
    }
}
