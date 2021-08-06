package pakoswdt;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pakoswdt.view.BuyerOverviewController;
import pakoswdt.view.SellerOverviewController;
import pakoswdt.view.StartingViewController;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private StartingViewController startingViewController;
    private SellerOverviewController sellerOverviewController;
    private BuyerOverviewController buyerOverviewController;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("WDT");

        initRootLayout();

        showStartingView();
        //showPersonOverview();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStartingView() {
        try {
            // Load starting view.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/StartingView.fxml"));
            AnchorPane startingView = (AnchorPane) loader.load();

            // Set starting view into the center of root layout.
            rootLayout.setCenter(startingView);

            StartingViewController controller = loader.getController();
            controller.setMainApp(this);
            startingViewController = controller;
            //rootLayoutController.set...(controller);??? MOŻE BYĆ WAŻNE ALE NIE WIEM CO ROBI
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showSellerOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SellerOverview.fxml"));
            AnchorPane sellerOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(sellerOverview);

            SellerOverviewController controller = loader.getController();
            controller.setMainApp(this);
            sellerOverviewController = controller;
            //TO SAMO CO METODĘ WYŻEJ
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showBuyerOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BuyerOverview.fxml"));
            AnchorPane buyerOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(buyerOverview);

            BuyerOverviewController controller = loader.getController();
            controller.setMainApp(this);
            buyerOverviewController = controller;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
