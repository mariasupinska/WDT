package pakoswdt.view;

import javafx.fxml.FXML;
import pakoswdt.MainApp;
import pakoswdt.model.Data;

public class BuyerOverviewController {
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
       // Data.getSeller().getName().setValue("XD");
    }

    @FXML
    private void handleNext() {}

    @FXML
    private void handlePrevious() {
        mainApp.showSellerOverview();
    }
}
