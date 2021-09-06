package pakoswdt;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pakoswdt.model.Buyer;
import pakoswdt.model.Data;
import pakoswdt.model.Product;
import pakoswdt.model.Vehicle;
import pakoswdt.model.legacy.LegacyBuyer;
import pakoswdt.model.legacy.LegacyData;
import pakoswdt.model.legacy.LegacyVehicle;
import pakoswdt.view.BuyerOverviewController;
import pakoswdt.view.ProductsOverviewController;
import pakoswdt.view.SellerOverviewController;
import pakoswdt.view.StartingViewController;

import javax.crypto.spec.GCMParameterSpec;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private StartingViewController startingViewController;
    private SellerOverviewController sellerOverviewController;
    private BuyerOverviewController buyerOverviewController;
    private ProductsOverviewController productsOverviewController;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("WDT");

        loadData();

        initRootLayout();

        showStartingView();
    }

    private void loadData() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String filePath = "/home/marysia/Downloads/NoweCokolwiek.json";

        String content = readFile(filePath);

        LegacyData legacyData = gson.fromJson(content, LegacyData.class);

        List<Buyer> buyers = legacyData.getBuyers().stream().map(this::convert).collect(Collectors.toList());

        Data.setBuyers(FXCollections.observableArrayList(buyers));
        Data.setProducts(legacyData.getProducts());
    }

    private Buyer convert(LegacyBuyer legacyBuyer) {
        return Buyer.builder().name(new SimpleStringProperty(legacyBuyer.getName())).
                street(new SimpleStringProperty(legacyBuyer.getAddress().getStreet())).
                city(new SimpleStringProperty(legacyBuyer.getAddress().getCity())).
                postalCode(new SimpleStringProperty(legacyBuyer.getAddress().getPostcode())).
                country(new SimpleStringProperty(legacyBuyer.getAddress().getCountry())).
                nip(new SimpleStringProperty(legacyBuyer.getNip())).
                vehicles(new SimpleListProperty<>(convert(legacyBuyer.getVehicles()))).
                deliveryStreet(new SimpleStringProperty(legacyBuyer.getDeliveryAddress().getStreet())).
                deliveryCity(new SimpleStringProperty(legacyBuyer.getDeliveryAddress().getCity())).
                deliveryPostalCode(new SimpleStringProperty(legacyBuyer.getDeliveryAddress().getPostcode())).
                deliveryCountry(new SimpleStringProperty(legacyBuyer.getDeliveryAddress().getCountry())).
                personRetrieving(new SimpleStringProperty(legacyBuyer.getPersonRetrieving())).
                personConfirming(new SimpleStringProperty(legacyBuyer.getPersonConfirming())).build();
    }

    private ObservableList<Vehicle> convert(List<LegacyVehicle> vehicles) {
        ObservableList<Vehicle> newVehicles = FXCollections.observableArrayList();
        vehicles.forEach(vehicle -> {
            newVehicles.add(new Vehicle(new SimpleStringProperty(vehicle.getBrand()), new SimpleStringProperty(vehicle.getRegistrationNumber())));
        });
        return newVehicles;
    }

    private String readFile(String path) {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded);
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

    public void showProductsOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ProductsOverview.fxml"));
            AnchorPane productsOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(productsOverview);

            ProductsOverviewController controller = loader.getController();
            controller.setMainApp(this);
            productsOverviewController = controller;
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
