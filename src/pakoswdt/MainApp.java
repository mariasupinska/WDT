package pakoswdt;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.hildan.fxgson.FxGson;
import pakoswdt.file.DataFile;
import pakoswdt.model.*;
import pakoswdt.model.legacy.LegacyBuyer;
import pakoswdt.model.legacy.LegacyData;
import pakoswdt.model.legacy.LegacySeller;
import pakoswdt.model.legacy.LegacyVehicle;
import pakoswdt.view.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private StartingViewController startingViewController;
    private SellerOverviewController sellerOverviewController;
    private BuyerOverviewController buyerOverviewController;
    private ProductsOverviewController productsOverviewController;
    private String oldDatabaseFilePath = "";
    private String newDatabaseFilePath = "";


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("WDT");

        initRootLayout();

        showStartingView();

        loadData();
    }

    private void loadData() {
        String dataPath = DataFile.getDataFile();

        if ( !dataPath.isEmpty() ) {
            loadNewData(dataPath);
        } else {
            showOldDatabaseFilePath();
            loadOldData(oldDatabaseFilePath);
            showNewDatabaseFilePath();
            showLogFilePath();
        }

        /*if ( !Data.getDefaultDatabasePath().isEmpty() ) {
            loadNewData(Data.getDefaultDatabasePath());
        } else {
            showOldDatabaseFilePath();
            loadOldData(oldDatabaseFilePath);
            showNewDatabaseFilePath();
            System.out.println();
        }*/

        /*
        if ( checkFileExistence("src/resources/New.json")  ) {
            loadNewData("src/resources/New.json");

        } else if ( checkFileExistence("src/resources/Old.json") ) {
            loadOldData("src/resources/Old.json");

        } else {
            new Alerts(AlertEnum.NO_FILE_FOUND, this.primaryStage.getOwner()).display();
        }

         */
    }

    private void showOldDatabaseFilePath() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/OldDatabaseFileDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Ścieżka do odczytu pliku z danymi");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(this.getPrimaryStage());
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        OldDatabaseFileDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMainApp(this);
        dialogStage.showAndWait();
    }

    private void showNewDatabaseFilePath() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/NewDatabaseFileDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Ścieżka do zapisu pliku z danymi");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(this.getPrimaryStage());
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        NewDatabaseFileDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMainApp(this);
        dialogStage.showAndWait();
    }

    private void showLogFilePath() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/LogPathDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Ścieżka do zapisu pliku z logami");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(this.getPrimaryStage());
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        LogPathDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMainApp(this);
        dialogStage.showAndWait();
    }

    //TODO: możliwe że niepotrzebne
    private boolean checkFileExistence(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    private void loadOldData(String filePath) {
        Gson gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        String content = readFile(filePath);

        LegacyData legacyData = gson.fromJson(content, LegacyData.class);

        List<Buyer> buyers = legacyData.getBuyers().stream().map(this::convert).collect(Collectors.toList());

        Data.setSeller(convert(legacyData.getSeller()));
        Data.setBuyers(FXCollections.observableArrayList(buyers));
        Data.setProducts(legacyData.getProducts());
    }

    private void loadNewData(String filePath) {
        Gson gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        String content = readFile(filePath);

        DataStore storeData = gson.fromJson(content, DataStore.class);

        Data.setSeller(storeData.getSeller());
        Data.setBuyers(FXCollections.observableArrayList(storeData.getBuyers()));
        Data.setProducts(storeData.getProducts());
        Data.setPackages(storeData.getPackages());
        Data.setDefaultDatabasePath(storeData.getDefaultDatabasePath());
        Data.setDefaultLogPath(storeData.getDefaultLogPath());
        Data.setDefaultInvoiceSummaryPath(storeData.getDefaultInvoiceSummaryPath());
        Data.setDefaultInvoicePath(storeData.getDefaultInvoicePath());
    }

    public void saveData() {
        Gson gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        String filePath = Data.getDefaultDatabasePath();

        DataStore data = new DataStore(Data.getSeller(), Data.getBuyersAsList(), Data.getProducts(), Data.getPackages(), Data.getDefaultDatabasePath(), Data.getDefaultLogPath(), Data.getDefaultInvoiceSummaryPath(), Data.getDefaultInvoicePath());

        String json = gson.toJson(data);

        writeFile(filePath, json);
    }

    private Seller convert(LegacySeller legacySeller) {
        return Seller.builder().name(new SimpleStringProperty(legacySeller.getName())).
                street(new SimpleStringProperty(legacySeller.getAddress().getStreet())).
                city(new SimpleStringProperty(legacySeller.getAddress().getCity())).
                postalCode(new SimpleStringProperty(legacySeller.getAddress().getPostcode())).
                country(new SimpleStringProperty(legacySeller.getAddress().getCountry())).
                nip(new SimpleStringProperty(legacySeller.getNip())).
                vehicles(new SimpleListProperty<>(convert(legacySeller.getVehicles()))).
                employees(new SimpleListProperty<>(FXCollections.observableArrayList())).build();

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
        return new String(encoded, StandardCharsets.UTF_8);
    }

    private void writeFile(String path, String content) {
        File target = new File(path);
        try {
            FileUtils.write(target, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            primaryStage.setMinHeight(530);
            primaryStage.setHeight(530);
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
            primaryStage.setMinHeight(800);
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
