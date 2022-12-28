package pakoswdt.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pakoswdt.MainApp;
import pakoswdt.model.AlertEnum;
import pakoswdt.model.Alerts;
import pakoswdt.model.Data;
import pakoswdt.model.Vehicle;

import java.util.Optional;

@Slf4j
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
    @FXML
    private TextField invoiceNumber;
    @FXML
    private TextField place;
    @FXML
    private DatePicker invoiceCreationDate;
    @FXML
    private ChoiceBox<Vehicle> vehicles;
    @FXML
    private ChoiceBox<String> employees;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        bind();
        addListeners();
        setValues();
        Data.setInvoiceSeller(Data.getSeller());
    }

    private void bind() {
        name.textProperty().bindBidirectional(Data.getSeller().getName());
        street.textProperty().bindBidirectional(Data.getSeller().getStreet());
        city.textProperty().bindBidirectional(Data.getSeller().getCity());
        postalCode.textProperty().bindBidirectional(Data.getSeller().getPostalCode());
        country.textProperty().bindBidirectional(Data.getSeller().getCountry());
        nip.textProperty().bindBidirectional(Data.getSeller().getNip());
        invoiceCreationDate.valueProperty().bindBidirectional(Data.getInvoice().getCreationDate());
        invoiceNumber.textProperty().bindBidirectional(Data.getInvoice().getNumber());
        place.textProperty().bindBidirectional(Data.getInvoice().getPlaceOfExtradition());
        vehicles.itemsProperty().bindBidirectional(Data.getSeller().getVehicles());
        employees.itemsProperty().bindBidirectional(Data.getSeller().getEmployees());
    }

    private void addListeners() {
        vehicles.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue != null ) {
                        Data.getInvoice().setTransport(newValue);
                    }
                } );

        employees.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue != null ) {
                        Data.getInvoice().setCreator(newValue);
                    }
                } );
    }

    private void setValues() {
        vehicles.setValue(Data.getInvoice().getTransport());
        employees.setValue(Data.getInvoice().getCreator());
    }

    @FXML
    private void handleAddPerson() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/NewPersonDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (Exception e) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, mainApp.getPrimaryStage()).display();
            log.error("Unhandled exception", e);
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Nowa osoba");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(mainApp.getPrimaryStage());
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        NewPersonDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.showAndWait();

        String person = controller.getPerson();
        if ( person != null ) {
            Data.getSeller().getEmployees().add(person);
            FXCollections.sort(Data.getSeller().getEmployees());
            employees.getSelectionModel().select(person);
        }
    }

    @FXML
    private void handleDeletePerson() {
        int selectedIndex = employees.getSelectionModel().getSelectedIndex();
        if ( selectedIndex >= 0 ) {
            Optional<ButtonType> result = new Alerts(AlertEnum.DELETING_PERSON, mainApp.getPrimaryStage()).display();
            if( result.get() == ButtonType.OK ) {
                employees.getItems().remove(selectedIndex);
            } else if ( result.get() == ButtonType.CANCEL ) {
                return;
            }

        } else {
            new Alerts(AlertEnum.NO_PERSON_SELECTED, mainApp.getPrimaryStage()).display();
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
            new Alerts(AlertEnum.UNKNOWN_ERROR, mainApp.getPrimaryStage()).display();
            log.error("Unhandled exception", e);
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
            Data.getSeller().getVehicles().add(vehicle);
            FXCollections.sort(Data.getSeller().getVehicles());
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
                } else if ( result.get() == ButtonType.CANCEL ) {
                    return;
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
        mainApp.showBuyerOverview();
    }

    private boolean isInputValid() {
        return StringUtils.isNoneBlank(name.textProperty().get(),
                street.textProperty().get(),
                city.textProperty().get(),
                postalCode.textProperty().get(),
                country.textProperty().get(),
                nip.textProperty().get(),
                invoiceNumber.textProperty().get(),
                place.textProperty().get())
                && invoiceCreationDate.valueProperty().get() != null
                && Data.getInvoice().getCreator() != null;
    }

    @FXML
    private void handlePrevious() {
        mainApp.showStartingView();
    }
}
