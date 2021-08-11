package pakoswdt.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pakoswdt.MainApp;
import pakoswdt.model.Data;
import pakoswdt.model.Vehicle;

import java.io.IOException;
import java.util.Optional;

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

        vehicles.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue != null ) {
                        Data.getInvoice().setTransport(newValue);
                    }
                } );

        vehicles.setValue(Data.getInvoice().getTransport()); //TODO: sprawdzić czzy działa gdy pojazd Sellera w Widoku Buyera lub vice versa

        employees.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue != null ) {
                        Data.getInvoice().setCreator(newValue);
                    }
                } );

        employees.setValue(Data.getInvoice().getCreator());
    }

    @FXML
    private void handleAddPerson() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/NewPersonDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setHeaderText("Usuwanie osoby");
            alert.setContentText("Czy na pewno chcesz usunąć wybraną osobę?");
            Optional<ButtonType> result = alert.showAndWait();
            if( result.get() == ButtonType.OK ) {
                employees.getItems().remove(selectedIndex);
            } else if ( result.get() == ButtonType.CANCEL ) {
                return;
            }

        } else {
            //Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Brak zaznaczenia");
            alert.setHeaderText("Nie wybrano żadnej osoby");
            alert.setContentText("Aby usunąć osobę należy ją najpierw wybrać z listy.");
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
    private void handleNext() {
        mainApp.showBuyerOverview();
    }

    @FXML
    private void handlePrevious() {
        mainApp.showStartingView();
    }
}
