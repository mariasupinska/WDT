package pakoswdt.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pakoswdt.MainApp;
import pakoswdt.model.Data;
import pakoswdt.model.Vehicle;

import java.io.IOException;

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
    private ChoiceBox<Vehicle> vehicles;


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
        vehicles.itemsProperty().bindBidirectional(Data.getSeller().getVehicles());

        vehicles.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue != null ) {
                        Data.getInvoice().setTransport(newValue);
                    }
                } );

        vehicles.setValue(Data.getInvoice().getTransport()); //TODO: sprawdzić czzy działa gdy pojazd Sellera w Widoku Buyera lub vice versa
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
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(mainApp.getPrimaryStage());
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        NewVehicleDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.showAndWait();

        Vehicle vehicle = controller.getVehicle();
        if ( vehicle != null ) vehicles.getItems().add(vehicle);
    }

    @FXML
    private void handleDeleteVehicle() {
        int selectedIndex = vehicles.getSelectionModel().getSelectedIndex();
        if ( selectedIndex >= 0 ) {
            vehicles.getItems().remove(selectedIndex);
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
