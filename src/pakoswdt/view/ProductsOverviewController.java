package pakoswdt.view;

import com.opencsv.bean.CsvToBeanBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import pakoswdt.MainApp;
import pakoswdt.model.Data;
import pakoswdt.model.Product;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductsOverviewController {
    private MainApp mainApp;

    @FXML
    private TextField filePath;
    @FXML
    private TableView<Product> productsTableView;
    @FXML
    private TableColumn<Product, String> name;
    @FXML
    private TableColumn<Product, Number> amount;
    @FXML
    private TableColumn<Product, String> unit;
    @FXML
    private TableColumn<Product, Number> unitWeight;
    @FXML
    private TableColumn<Product, Number> netWeight;
    @FXML
    private TableColumn<Product, String> packageType;
    @FXML
    private TableColumn<Product, String> packagesAmount;
    @FXML
    private TableColumn<Product, String> packageUnitWeight;
    @FXML
    private TableColumn<Product, String> packagesTotalWeight;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
        name.setCellValueFactory(cellData -> cellData.getValue().getName());
        amount.setCellValueFactory(cellData -> cellData.getValue().getAmount());
        unit.setCellValueFactory(cellData -> cellData.getValue().getUnit());
        unitWeight.setCellValueFactory(cellData -> cellData.getValue().getUnitWeight());
        netWeight.setCellValueFactory(cellData -> cellData.getValue().getNetWeight());
    }

    @FXML
    private void readCsvFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter csvExtFilter = new FileChooser.ExtensionFilter("Csv files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(csvExtFilter);
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if ( file != null && file.exists() ) {
            filePath.setText(file.getPath());
            List<Product> products = new CsvToBeanBuilder<Product>(new FileReader(file))
                    .withType(Product.class)
                    .withSkipLines(1)
                    .build()
                    .parse()
                    .stream()
                    .filter(product -> !product.shouldBeIgnored())
                    .map(product -> product.enrich(Data.getProducts()))
                    .collect(Collectors.toList());

            ObservableList<Product> observableProducts = FXCollections.observableArrayList(products); //TODO: dalej rozwinąć

            productsTableView.setItems(observableProducts);
        }
    }
}
