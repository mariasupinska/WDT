package pakoswdt.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.Map;

public class Data {
    private static Invoice invoice;
    private static ObservableList<Buyer> buyers;
    private static Map<String, BigDecimal> products;

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

    private static ObservableList<Buyer> createBuyers() {
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
        vehicles.add(new Vehicle(new SimpleStringProperty(""), new SimpleStringProperty("")));
        vehicles.add(new Vehicle(new SimpleStringProperty("Audi"), new SimpleStringProperty("BI 12345")));
        vehicles.add(new Vehicle(new SimpleStringProperty("Porsche"), new SimpleStringProperty("BI 98765")));
        FXCollections.sort(vehicles);

        ObservableList<Vehicle> vehiclesTwo = FXCollections.observableArrayList();
        vehiclesTwo.add(new Vehicle(new SimpleStringProperty(""), new SimpleStringProperty("")));
        vehiclesTwo.add(new Vehicle(new SimpleStringProperty("Lamborghini"), new SimpleStringProperty("BI 12345")));
        vehiclesTwo.add(new Vehicle(new SimpleStringProperty("Tesla"), new SimpleStringProperty("BI 98765")));
        FXCollections.sort(vehiclesTwo);

        ObservableList<Vehicle> vehiclesThree = FXCollections.observableArrayList();
        vehiclesThree.add(new Vehicle(new SimpleStringProperty(""), new SimpleStringProperty("")));
        vehiclesThree.add(new Vehicle(new SimpleStringProperty("Bentley"), new SimpleStringProperty("BI 12345")));
        vehiclesThree.add(new Vehicle(new SimpleStringProperty("Ferrari"), new SimpleStringProperty("BI 98765")));
        FXCollections.sort(vehiclesThree);

        Buyer buyerOne = Buyer.builder().name(new SimpleStringProperty("Buyer nr 1")).
                street(new SimpleStringProperty("Biała 1")).
                city(new SimpleStringProperty("Warszawa")).
                postalCode(new SimpleStringProperty("12-345")).
                country(new SimpleStringProperty("Polska")).
                nip(new SimpleStringProperty("PL 123456789")).
                vehicles(new SimpleListProperty<>(vehicles)).
                deliveryStreet(new SimpleStringProperty("")).
                deliveryCity(new SimpleStringProperty("")).
                deliveryPostalCode(new SimpleStringProperty("")).
                deliveryCountry(new SimpleStringProperty("")).
                personRetrieving(new SimpleStringProperty("")).
                personConfirming(new SimpleStringProperty("")).build();

        Buyer buyerTwo = Buyer.builder().name(new SimpleStringProperty("Buyer nr 2")).
                street(new SimpleStringProperty("Biała 2")).
                city(new SimpleStringProperty("Warszawa")).
                postalCode(new SimpleStringProperty("12-345")).
                country(new SimpleStringProperty("Polska")).
                nip(new SimpleStringProperty("PL 123456789")).
                vehicles(new SimpleListProperty<>(vehiclesTwo)).
                deliveryStreet(new SimpleStringProperty("")).
                deliveryCity(new SimpleStringProperty("")).
                deliveryPostalCode(new SimpleStringProperty("")).
                deliveryCountry(new SimpleStringProperty("")).
                personRetrieving(new SimpleStringProperty("")).
                personConfirming(new SimpleStringProperty("")).build();

        Buyer buyerThree = Buyer.builder().name(new SimpleStringProperty("Buyer nr 3")).
                street(new SimpleStringProperty("Biała 3")).
                city(new SimpleStringProperty("Warszawa")).
                postalCode(new SimpleStringProperty("12-345")).
                country(new SimpleStringProperty("Polska")).
                nip(new SimpleStringProperty("PL 123456789")).
                vehicles(new SimpleListProperty<>(vehiclesThree)).
                deliveryStreet(new SimpleStringProperty("")).
                deliveryCity(new SimpleStringProperty("")).
                deliveryPostalCode(new SimpleStringProperty("")).
                deliveryCountry(new SimpleStringProperty("")).
                personRetrieving(new SimpleStringProperty("")).
                personConfirming(new SimpleStringProperty("")).build();

        ObservableList<Buyer> buyers = FXCollections.observableArrayList();
        buyers.add(buyerOne);
        buyers.add(buyerTwo);
        buyers.add(buyerThree);

        return buyers;
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

    public static void setBuyers(ObservableList<Buyer> buyers) {
        Data.buyers = buyers;
    }

    public static Map<String, BigDecimal> getProducts() {
        return products;
    }

    public static void setProducts(Map<String, BigDecimal> products) {
        Data.products = products;
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
}
