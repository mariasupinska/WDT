package pakoswdt.controller;

import com.opencsv.bean.CsvToBeanBuilder;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.converter.BigDecimalStringConverter;
import org.apache.commons.lang3.StringUtils;
import pakoswdt.MainApp;
import pakoswdt.file.ExcelWriter;
import pakoswdt.model.Package;
import pakoswdt.model.*;
import pakoswdt.tableCell.EditingNumberCell;
import pakoswdt.tableCell.EditingNumberIntegerCell;
import pakoswdt.tableCell.EditingStringCell;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
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
    @FXML
    private TextField palettesWeight;

    @FXML
    private Button nextButton;

    private ArrayList<String> packageTypes = new ArrayList<String>(Arrays.asList("Brak", "Folia", "Karton", "Papier"));
    private ArrayList<String> packageChoices = new ArrayList<String>(Arrays.asList("Brak", "Folia", "Karton", "Papier"));

    private Map<String, Package> multipackages = new HashMap<>();

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    @FXML
    public void initialize() {
        filePath.setText(Data.inputFilePathProperty().get());

        productsTableView.setEditable(true);

        productsTableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );

        name.setCellValueFactory(cellData -> cellData.getValue().getName());
        name.setCellFactory((TableColumn<Product, String> p) -> new EditingStringCell());
        name.setOnEditCommit(
                event -> {
                    getProduct(event).getName().setValue(event.getNewValue());
                    flushButton();
                }
        );

        amount.setCellValueFactory(cellData -> cellData.getValue().getAmount());
        amount.setCellFactory((TableColumn<Product, Number> p) -> new EditingNumberCell(0));
        amount.setOnEditCommit(
                event -> {
                    Product editedProduct = getProduct(event);
                    editedProduct.amount().setValue(event.getNewValue());

                    if ( editedProduct.getUnitWeight() != null ) {
                        recalculateNetWeight(editedProduct);
                    }
                    flushButton();
                }
        );

        unit.setCellValueFactory(cellData -> cellData.getValue().getUnit());
        unit.setCellFactory((TableColumn<Product, String> p) -> new EditingStringCell());
        unit.setOnEditCommit(
                event -> {
                    getProduct(event).getUnit().setValue(event.getNewValue());
                    flushButton();
                }
        );

        unitWeight.setCellValueFactory(cellData -> cellData.getValue().getUnitWeight());
        unitWeight.setCellFactory((TableColumn<Product, Number> p) -> new EditingNumberCell(3));
        unitWeight.setOnEditCommit(
                event -> {
                    Product editedProduct = getProduct(event);
                    editedProduct.unitWeight().setValue(event.getNewValue());
                    recalculateNetWeight(editedProduct);
                    flushButton();
                }
        );

        netWeight.setCellValueFactory(cellData -> cellData.getValue().getNetWeight());
        netWeight.setCellFactory((TableColumn<Product, Number> p) -> new EditingNumberCell(2));
        netWeight.setOnEditCommit(
                event -> {
                    Product editedProduct = getProduct(event);
                    editedProduct.netWeight().setValue(event.getNewValue());
                    recalculateUnitWeight(editedProduct);
                    flushButton();
                }
        );

        packageType.setCellValueFactory(cellData -> cellData.getValue().getProductPackage().getType());
        packageType.setCellFactory((TableColumn<Product, String> p) -> new EditingStringCell());
        packageType.setEditable(false);
        packageType.setOnEditCommit(
                event -> {
                    getProduct(event).getProductPackage().getType().setValue(event.getNewValue());
                    flushButton();
                }
        );

        packagesAmount.setCellValueFactory(cellData -> cellData.getValue().getProductPackage().getAmount());
        packagesAmount.setCellFactory((TableColumn<Product, Number> p) -> new EditingNumberIntegerCell(0));
        packagesAmount.setOnEditCommit(
                event -> {
                    getProduct(event).getProductPackage().amount().setValue(event.getNewValue());
                    productsTableView.refresh();
                    flushButton();
                }
        );

        packageUnitWeight.setCellValueFactory(cellData -> cellData.getValue().getProductPackage().getWeight());
        packageUnitWeight.setCellFactory((TableColumn<Product, Number> p) -> new EditingNumberCell(3));
        packageUnitWeight.setOnEditCommit(
                event -> {
                    getProduct(event).getProductPackage().weight().setValue(event.getNewValue());
                    productsTableView.refresh();
                    flushButton();
                }
        );

        packagesTotalWeight.setCellValueFactory(cellData -> {
            Package productPackage = cellData.getValue().getProductPackage();
            return recalculateTotalWeight(productPackage);
        });
        packagesTotalWeight.setCellFactory((TableColumn<Product, Number> p) -> new EditingNumberCell(2));
        packagesTotalWeight.setEditable(false);

        palettesWeight.textProperty().bindBidirectional(Data.getInvoice().getPalettes());

        productsTableView.setItems(Data.getTableProducts());

        flushButton();
    }

    private void flushButton() {
        if (isInputValid()) {
            this.nextButton.setStyle("-fx-background-color: #4dd922;\n" +
                    "    -fx-text-fill: black;\n");
        } else {
            this.nextButton.setStyle("");
        }
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
            List<Product> products = new CsvToBeanBuilder<Product>(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8))
                    .withType(Product.class)
                    .build()
                    .parse()
                    .stream()
                    .filter(product -> !product.shouldBeIgnored())
                    .map(product -> product.enrich(Data.getProducts()))
                    .collect(Collectors.toList());

            List<Product> mergedProducts = mergeProducts(products);
            List<Product> distinctProducts = mergedProducts.stream().distinct().collect(Collectors.toList());

            ObservableList<Product> observableProducts = FXCollections.observableArrayList(distinctProducts);

            Data.setTableProducts(observableProducts);
            Data.setInputFilePath(file.getPath());

            productsTableView.setItems(observableProducts);

            filePath.textProperty().bindBidirectional(Data.inputFilePathProperty());
        }
    }

    private List<Product> mergeProducts(List<Product> products) {
        List<Product> mergedProducts = new ArrayList<>();
        mergedProducts.add(products.get(0));

        for ( int i = 1; i < products.size(); i++ ) {
            Product previous = products.get(i-1);
            Product current = products.get(i);
            if ( isSplittedRow(previous, current) ) {
                mergeProducts(previous, current);
            } else {
                mergedProducts.add(current);
            }
        }

        return mergedProducts;
    }

    private boolean isSplittedRow(Product previous, Product current) {
        return previous.getNumber().getValue().equals(current.getNumber().getValue()) &&
                previous.getAmount().getValue().equals(current.getAmount().getValue()) &&
                previous.getUnit().getValue().equals(current.getUnit().getValue());
    }

    private void mergeProducts(Product toKeep, Product toIgnore) {
        toKeep.getName().setValue(toKeep.getName().getValue().trim() + toIgnore.getName().getValue().trim());
    }

    @FXML
    private void handleSetPackage() {
        String packageName;
        ObservableList<Product> selectedProducts = productsTableView.getSelectionModel().getSelectedItems();

        if ( selectedProducts.isEmpty() ) {
            new Alerts(AlertEnum.NO_ITEMS_SELECTED, mainApp.getPrimaryStage()).display();
            return;
        }

        Optional<String> result = showChoosePackageDialog(packageChoices);

        if ( result.isPresent() ) {
            packageName = result.get();
        } else return;

        if ( multipackages.get(packageName) != null ) {

            for (Product p : selectedProducts) {
                p.setProductPackage(multipackages.get(packageName));
            }

        } else {

            for (Product p : selectedProducts) {
                Package newPackage = new Package();
                newPackage.getType().setValue(packageName);

                p.setProductPackage(newPackage);

                BigDecimal lastPackageWeight = Data.getPackages().get(p.generateKeyWithPackage());
                if (lastPackageWeight != null) {
                    p.getProductPackage().weight().setValue(lastPackageWeight);
                }

                if ("Brak".equals(packageName)) {
                    p.getProductPackage().amount().setValue(new BigDecimal("0.0").setScale(0, RoundingMode.HALF_UP));
                    p.getProductPackage().weight().setValue(new BigDecimal("0.0").setScale(3, RoundingMode.HALF_UP));
                }
            }
        }
        productsTableView.refresh();
        flushButton();
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
    private void handleSetMultiPackage() {
        String packageName;
        ObservableList<Product> selectedProducts = productsTableView.getSelectionModel().getSelectedItems();

        if ( selectedProducts.isEmpty() ) {
            new Alerts(AlertEnum.NO_ITEMS_SELECTED, mainApp.getPrimaryStage()).display();
            return;
        }

        Optional<String> packageResult = showChoosePackageDialog(packageTypes);

        if ( packageResult.isPresent() ) {
            packageName = packageResult.get();
        } else return;

        Optional<String> inputWeight = showMultiPackageWeightDialog();

        if (!inputWeight.isPresent()) {
            return;
        }
        String weightResult = inputWeight
                .filter(val -> !StringUtils.isBlank(val))
                .map(val -> val.replace(",", "."))
                .orElse("0");
        BigDecimal weight = BigDecimal.valueOf(Double.parseDouble(weightResult)).setScale(3, RoundingMode.HALF_UP);

        int multipackageNumber = 0;
        for ( String packageType : packageChoices) {
            if ( packageType.contains(".") ) multipackageNumber++;
        }

        packageName = packageName + " " + (multipackageNumber+1) + ".";

        Package newPackage = new Package();
        newPackage.getType().setValue(packageName);
        newPackage.weight().setValue(weight);
        newPackage.amount().setValue(1);

        packageChoices.add(packageName);
        multipackages.put(packageName, newPackage);

        for ( Product p : selectedProducts ) {
            p.setProductPackage(newPackage);
        }

        productsTableView.refresh();
        flushButton();
    }

    private static Optional<String> showMultiPackageWeightDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Podaj wagę tworzonego opakowania zbiorczego");
        dialog.setContentText("Podaj wagę tworzonego opakowania zbiorczego");
        dialog.setResizable(false);

        return dialog.showAndWait();
    }

    @FXML
    private void handleGenerate() {
        if ( !isInputValid() ) {
            new Alerts(AlertEnum.NOT_FILLED_FIELDS, mainApp.getPrimaryStage()).display();
            return;
        }

        savePackagesUnitWeightMap();
        saveProductsWeightMap();
        mainApp.saveData();

        InvoiceSummary invoiceSummary = new InvoiceSummary(productsTableView.getItems(), new BigDecimalStringConverter().fromString(Data.getInvoice().getPalettes().get()));
        Data.getInvoice().setSummary(invoiceSummary);

        ExcelWriter excelWriter = new ExcelWriter(mainApp, Data.getInvoice(), productsTableView.getItems());
        excelWriter.createExcelFile();
    }

    private boolean isInputValid() {
        ObservableList<Product> products = productsTableView.getItems();
        return !products.isEmpty() && products.stream().allMatch(this::isProductInputValid);
    }

    private boolean isProductInputValid(Product product) {
        return StringUtils.isNoneBlank(
                product.getName().get(),
                product.getUnit().get(),
                product.productPackageType())
                && product.getAmount().getValue() != 0
                && product.getUnitWeight().getValue() != 0
                && product.getNetWeight().getValue() != 0
                && isProductPackageValid(product.getProductPackage());
    }

    private boolean isProductPackageValid(Package productPackage) {
        if (productPackage == null) return false;

        double amount = productPackage.getAmount() == null ? 0 : productPackage.getAmount().get();
        double weight = productPackage.getWeight() == null ? 0 : productPackage.getWeight().get();

        if ("Brak".equals(productPackage.getJustType())) {
            return amount == 0 && weight == 0;
        }

        return amount != 0 && weight != 0;
    }

    private void savePackagesUnitWeightMap() {
        for ( Product p: productsTableView.getItems() ) {
            if ( p.getProductPackage().getType() != null && p.getProductPackage().getWeight() != null && !p.getProductPackage().isMultiPackage() ) {
                Data.getPackages().put(p.generateKeyWithPackage(), BigDecimal.valueOf(p.getProductPackage().getWeight().get()));
            }
        }
    }

    private void saveProductsWeightMap() {
        for ( Product p: productsTableView.getItems() ) {
            Data.getProducts().put(p.generateKey(), BigDecimal.valueOf(p.getUnitWeight().get()));
        }
    }

    @FXML
    private void handlePrevious() {
        mainApp.showBuyerOverview();
    }
}
