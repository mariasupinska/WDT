package pakoswdt.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Data {
    private static Invoice invoice;
    private static Seller seller;
    private static ObservableList<Buyer> buyers = FXCollections.observableArrayList();
    private static Map<String, BigDecimal> products = new HashMap<>();
    private static Map<String, BigDecimal> packages = new HashMap<>();
    private static ObservableList<Product> tableProducts = FXCollections.observableArrayList();
    private static String defaultInvoiceSummaryPath = "";

    public Data() {}

    public static void createNewWDT() {
        Data.invoice = new Invoice();
    }

    public static Seller getSeller() {
        return Data.seller;
    }

    public static void setSeller(Seller seller) {
        Data.seller = seller;
    }

    public static Buyer getBuyer() {
        return Data.invoice.getBuyer();
    }

    public static ObservableList<Buyer> getBuyers() {
        return Data.buyers;
    }

    public static List<Buyer> getBuyersAsList() {
        return Data.getBuyers().stream().collect(Collectors.toList());
    }

    public static void setBuyers(ObservableList<Buyer> buyers) {
        Data.buyers = buyers;
    }

    public static Map<String, BigDecimal> getProducts() {
        return products;
    }

    public static void setProducts(Map<String, BigDecimal> products) {
        Data.products = products;
    }

    public static Map<String, BigDecimal> getPackages() {
        return packages;
    }

    public static void setPackages(Map<String, BigDecimal> packages) {
        Data.packages = packages;
    }

    public static Vehicle getTransport() {
        return Data.invoice.getTransport();
    }

    public static void setTransport(Vehicle vehicle) {
        Data.getInvoice().setTransport(vehicle);
    }

    public static Invoice getInvoice() {
        return invoice;
    }

    public static ObservableList<Product> getTableProducts() {
        return tableProducts;
    }

    public static void setTableProducts(ObservableList<Product> tableProducts) {
        Data.tableProducts = tableProducts;
    }

    public static String getDefaultInvoiceSummaryPath() {
        return defaultInvoiceSummaryPath;
    }

    public static void setDefaultInvoiceSummaryPath(String defaultInvoiceSummaryPath) {
        Data.defaultInvoiceSummaryPath = defaultInvoiceSummaryPath;
    }
}
