package pakoswdt.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Data {
    private static Invoice invoice;
    private static ObservableList<Buyer> buyers = FXCollections.observableArrayList();
    private static Map<String, BigDecimal> products = new HashMap<>();
    private static Map<String, BigDecimal> packages = new HashMap<>();
    private static ObservableList<Product> tableProducts = FXCollections.observableArrayList();

    public Data() {

    }

    public static void createNewWDT() {
        Data.invoice = new Invoice();
        Data.invoice.setSeller(Data.createSeller());
    }

    private static Seller createSeller() {
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
        vehicles.add(new Vehicle(new SimpleStringProperty(""), new SimpleStringProperty("")));
        vehicles.add(new Vehicle(new SimpleStringProperty("Renault"), new SimpleStringProperty("BI 12345")));
        vehicles.add(new Vehicle(new SimpleStringProperty("Mercedes"), new SimpleStringProperty("BI 98765")));
        FXCollections.sort(vehicles);

        ObservableList<String> employees = FXCollections.observableArrayList();
        employees.add("Person nr 1");
        employees.add("Person nr 2");
        employees.add("Person nr 3");
        FXCollections.sort(employees);

        Seller seller = Seller.builder().name(new SimpleStringProperty("Seller")).
                street(new SimpleStringProperty("Zielona 0")).
                city(new SimpleStringProperty("Białystok")).
                postalCode(new SimpleStringProperty("99-2137")).
                country(new SimpleStringProperty("Polska")).
                nip(new SimpleStringProperty("PL 123456789")).
                vehicles(new SimpleListProperty<>(vehicles)).
                employees(new SimpleListProperty<>(employees)).build();

        return seller;
    }

    public static Seller getSeller() {
        return Data.invoice.getSeller();
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
}
