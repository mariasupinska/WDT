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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.hildan.fxgson.FxGson;
import pakoswdt.controller.*;
import pakoswdt.file.DataFile;
import pakoswdt.model.*;
import pakoswdt.model.legacy.LegacyBuyer;
import pakoswdt.model.legacy.LegacyData;
import pakoswdt.model.legacy.LegacySeller;
import pakoswdt.model.legacy.LegacyVehicle;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle("WDT");

            initRootLayout();

            showStartingView();

            loadData();

            updateLog4jConfiguration(Data.getDefaultLogPath());
            log.info("Starting application...");
        } catch (Exception ex) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, primaryStage).display();
            log.error("Unhandled exception: ", ex);
        }

    }

    public void updateLog4jConfiguration(String logFile) {
        Properties props = new Properties();
        try {
            InputStream configStream = getClass().getResourceAsStream( "/log4j.properties");
            props.load(configStream);
            configStream.close();
        } catch (Exception ex) {
            new Alerts(AlertEnum.CANNOT_LOAD_FILE, primaryStage).display();
            log.error("Unhandled exception: ", ex);
        }

        String actualProperty = props.getProperty("log4j.appender.FileAppender.rollingPolicy.fileNamePattern");
        props.setProperty("log4j.appender.FileAppender.rollingPolicy.fileNamePattern",
                logFile + File.separator + actualProperty);

        LogManager.resetConfiguration();
        PropertyConfigurator.configure(props);
    }

    private void loadData() {
        String dataPath = DataFile.getDataFile();

        if ( !dataPath.isEmpty() && isPathValid(dataPath) ) {
            loadNewData(dataPath);
        } else {
            showOldDatabaseFilePath();
            loadOldData(oldDatabaseFilePath);
            showNewDatabaseFilePath();
            showLogFilePath();
        }
    }

    private boolean isPathValid(String path) {
        if ( Files.exists(Paths.get(path)) ) return true;
        else {
            new Alerts(AlertEnum.INVALID_PATH, primaryStage).display();
            return false;
        }
    }

    private void showOldDatabaseFilePath() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/view/OldDatabaseFileDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (Exception e) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, primaryStage).display();
            log.error("Unhandled exception", e);
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
        loader.setLocation(MainApp.class.getResource("/view/NewDatabaseFileDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (Exception e) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, primaryStage).display();
            log.error("Unhandled exception", e);
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
        loader.setLocation(MainApp.class.getResource("/view/LogPathDialog.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (Exception e) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, primaryStage).display();
            log.error("Unhandled exception", e);
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
        } catch (Exception e) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, primaryStage).display();
            log.error("Unhandled exception", e);
        }
        return new String(encoded, StandardCharsets.UTF_8);
    }

    private void writeFile(String path, String content) {
        File target = new File(path);
        try {
            FileUtils.write(target, content, StandardCharsets.UTF_8);
        } catch (Exception e) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, primaryStage).display();
            log.error("Unhandled exception", e);
        }
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, primaryStage).display();
            log.error("Unhandled exception", e);
        }
    }

    public void showStartingView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/StartingView.fxml"));
            AnchorPane startingView = (AnchorPane) loader.load();

            rootLayout.setCenter(startingView);

            StartingViewController controller = loader.getController();
            controller.setMainApp(this);
            startingViewController = controller;
        } catch (Exception e) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, primaryStage).display();
            log.error("Unhandled exception", e);
        }
    }

    public void showSellerOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/SellerOverview.fxml"));
            AnchorPane sellerOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(sellerOverview);

            SellerOverviewController controller = loader.getController();
            controller.setMainApp(this);
            sellerOverviewController = controller;
        } catch (Exception e) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, primaryStage).display();
            log.error("Unhandled exception", e);
        }
    }

    public void showBuyerOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/BuyerOverview.fxml"));
            AnchorPane buyerOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(buyerOverview);

            BuyerOverviewController controller = loader.getController();
            controller.setMainApp(this);
            buyerOverviewController = controller;
        } catch (Exception e) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, primaryStage).display();
            log.error("Unhandled exception", e);
        }
    }

    public void showProductsOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/ProductsOverview.fxml"));
            AnchorPane productsOverview = (AnchorPane) loader.load();

            primaryStage.setMinHeight(800);
            rootLayout.setCenter(productsOverview);

            ProductsOverviewController controller = loader.getController();
            controller.setMainApp(this);
            productsOverviewController = controller;
        } catch (Exception e) {
            new Alerts(AlertEnum.UNKNOWN_ERROR, primaryStage).display();
            log.error("Unhandled exception", e);
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
