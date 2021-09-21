package pakoswdt.view;

import com.opencsv.bean.CsvToBeanBuilder;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import pakoswdt.MainApp;
import pakoswdt.model.Package;
import pakoswdt.model.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    private TableColumn<Product, Number> packagesAmount;
    @FXML
    private TableColumn<Product, Number> packageUnitWeight;
    @FXML
    private TableColumn<Product, Number> packagesTotalWeight;

    private ArrayList<String> packageTypes = new ArrayList<String>(Arrays.asList("Brak", "Folia", "Karton", "Papier"));

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
        productsTableView.setEditable(true);

        productsTableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );

        name.setCellValueFactory(cellData -> cellData.getValue().getName());
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                event -> {
                    getProduct(event).getName().setValue(event.getNewValue());
                }
        );

        amount.setCellValueFactory(cellData -> cellData.getValue().getAmount());
        amount.setCellFactory(TextFieldTableCell.forTableColumn(new StringNumberConverter()));
        amount.setOnEditCommit(
                event -> {
                    Product editedProduct = getProduct(event);
                    editedProduct.amount().setValue(event.getNewValue());

                    if ( editedProduct.getUnitWeight() != null ) {
                        recalculateNetWeight(editedProduct);
                    }
                }
        );

        unit.setCellValueFactory(cellData -> cellData.getValue().getUnit());
        unit.setCellFactory(TextFieldTableCell.forTableColumn());
        unit.setOnEditCommit(
                event -> {
                    getProduct(event).getUnit().setValue(event.getNewValue());
                }
        );

        unitWeight.setCellValueFactory(cellData -> cellData.getValue().getUnitWeight());
        unitWeight.setCellFactory(TextFieldTableCell.forTableColumn(new StringNumberConverter()));
        unitWeight.setOnEditCommit(
                event -> {
                    Product editedProduct = getProduct(event);
                    editedProduct.unitWeight().setValue(event.getNewValue());
                    recalculateNetWeight(editedProduct);
                }
        );

        netWeight.setCellValueFactory(cellData -> cellData.getValue().getNetWeight());
        netWeight.setCellFactory(TextFieldTableCell.forTableColumn(new StringNumberConverter()));
        netWeight.setOnEditCommit(
                event -> {
                    Product editedProduct = getProduct(event);
                    editedProduct.netWeight().setValue(event.getNewValue());
                    recalculateUnitWeight(editedProduct);
                }
        );

        packageType.setCellValueFactory(cellData -> cellData.getValue().getProductPackage().getType());
        packageType.setCellFactory(TextFieldTableCell.forTableColumn());
        packageType.setEditable(false);
        packageType.setOnEditCommit(
                event -> {
                    getProduct(event).getProductPackage().getType().setValue(event.getNewValue());
                }
        );

        packagesAmount.setCellValueFactory(cellData -> cellData.getValue().getProductPackage().getAmount());
        packagesAmount.setCellFactory(TextFieldTableCell.forTableColumn(new StringNumberConverter()));
        packagesAmount.setOnEditCommit(
                event -> {
                    getProduct(event).getProductPackage().amount().setValue(event.getNewValue());
                    productsTableView.refresh();
                }
        );

        packageUnitWeight.setCellValueFactory(cellData -> cellData.getValue().getProductPackage().getWeight());
        packageUnitWeight.setCellFactory(TextFieldTableCell.forTableColumn(new StringNumberConverter()));
        packageUnitWeight.setOnEditCommit(
                event -> {
                    getProduct(event).getProductPackage().weight().setValue(event.getNewValue());
                    productsTableView.refresh();
                }
        );

        packagesTotalWeight.setCellValueFactory(cellData -> {
            Package productPackage = cellData.getValue().getProductPackage();
            return recalculateTotalWeight(productPackage);
        });
        packagesTotalWeight.setCellFactory(TextFieldTableCell.forTableColumn(new StringNumberConverter()));
        packagesTotalWeight.setEditable(false);

    }

    private Product getProduct(TableColumn.CellEditEvent<Product, ? extends Object> event) {
        int rowNumber = event.getTablePosition().getRow();
        return event.getTableView().getItems().get(rowNumber);
    }

    private void recalculateUnitWeight(Product editedProduct) {
        BigDecimal netWeight = BigDecimal.valueOf(editedProduct.getNetWeight().get());
        BigDecimal amount = BigDecimal.valueOf(editedProduct.getAmount().get());
        if ( editedProduct.getUnitWeight() == null ) editedProduct.setUnitWeight(new SimpleDoubleProperty());
        editedProduct.getUnitWeight().setValue(netWeight.divide(amount, RoundingMode.HALF_EVEN).doubleValue());
        productsTableView.refresh();
    }

    private void recalculateNetWeight(Product editedProduct) {
        BigDecimal amount = BigDecimal.valueOf(editedProduct.getAmount().get());
        BigDecimal unitWeight = BigDecimal.valueOf(editedProduct.getUnitWeight().get());
        if ( editedProduct.getNetWeight() == null ) editedProduct.setNetWeight(new SimpleDoubleProperty());
        editedProduct.getNetWeight().setValue(amount.multiply(unitWeight).doubleValue());
        productsTableView.refresh();
    }

    private SimpleDoubleProperty recalculateTotalWeight(Package editedPackage) {
        if ( editedPackage.getAmount() != null && editedPackage.getWeight() != null ) {
            BigDecimal amount = BigDecimal.valueOf(editedPackage.getAmount().get());
            BigDecimal weight = BigDecimal.valueOf(editedPackage.getWeight().get());
            BigDecimal result = amount.multiply(weight);
            return new SimpleDoubleProperty(result.doubleValue());
        } else return null;
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

            ObservableList<Product> observableProducts = FXCollections.observableArrayList(products);

            productsTableView.setItems(observableProducts);
        }
    }

    @FXML
    private void handleSetPackage() {
        String packageName;
        ObservableList<Product> selectedProducts = productsTableView.getSelectionModel().getSelectedItems();

        if ( selectedProducts.isEmpty() ) { //|| selectedProducts == null)
            new Alerts(AlertEnum.NO_ITEMS_SELECTED, mainApp.getPrimaryStage()).display();
            return;
        }

        Optional<String> result = showChoosePackageDialog(packageTypes);

        if ( result.isPresent() ) {
            packageName = result.get().trim();
        } else return;

        for ( Product p : selectedProducts ) {
            p.getProductPackage().getType().setValue(packageName);

            BigDecimal lastPackageWeight = Data.getPackages().get(p.generateKeyWithPackage());
            if ( lastPackageWeight != null ) {
                p.getProductPackage().weight().setValue(lastPackageWeight);
            }

            if ( "Brak".equals(packageName) ) {
                p.getProductPackage().amount().setValue(new BigDecimal("0.0").setScale(0, RoundingMode.HALF_UP));
                p.getProductPackage().weight().setValue(new BigDecimal("0.0").setScale(3, RoundingMode.HALF_UP));
            }
        }

        productsTableView.refresh();
    }

    private static Optional<String> showChoosePackageDialog(List<String> optionsList) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, optionsList);
        dialog.setTitle("Ustaw opakowanie");
        dialog.setHeaderText("Wybór opakowania dla zaznaczonych przedmiotów");
        dialog.setContentText("Wybierz typ opakowania, do którego zapakowane zostaną zaznaczone produkty.");
        dialog.setResizable(false);

        return dialog.showAndWait();
    }

    @FXML
    private void handleSave() {
        savePackagesUnitWeightMap();
        mainApp.saveData();
    }

    private void savePackagesUnitWeightMap() {
        for ( Product p: productsTableView.getItems() ) {
            if ( p.getProductPackage().getType() != null && p.getProductPackage().getWeight() != null ) {
                Data.getPackages().put(p.generateKeyWithPackage(), BigDecimal.valueOf(p.getProductPackage().getWeight().get()));
            }
        }
    }
}
