package pakoswdt.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Data {
    private static Invoice invoice;

    public Data() {

    }

    public static void createNewWDT() {
        Data.invoice = new Invoice();
        Data.invoice.setSeller(Data.createSeller());
    }

    public static Seller getSeller() {
        return Data.invoice.getSeller();
    }

    public static Vehicle getTransport() {
        return Data.invoice.getTransport();
    }

    public static void setTransport(Vehicle vehicle) {
        Data.getInvoice().setTransport(vehicle);
    }

    private static Seller createSeller() {
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
        vehicles.add(new Vehicle(new SimpleStringProperty(""), new SimpleStringProperty("")));
        vehicles.add(new Vehicle(new SimpleStringProperty("Renault"), new SimpleStringProperty("BI 12345")));
        vehicles.add(new Vehicle(new SimpleStringProperty("Mercedes"), new SimpleStringProperty("BI 98765")));
        FXCollections.sort(vehicles);

        ObservableList<String> employees = FXCollections.observableArrayList();
        employees.add("Jan Kowalski");
        employees.add("Adam Nowak");
        employees.add("Andrzej Dąbrowski");
        FXCollections.sort(employees);

        Seller seller = Seller.builder().name(new SimpleStringProperty("")).
                street(new SimpleStringProperty("Zielona 0")).
                city(new SimpleStringProperty("Białystok")).
                postalCode(new SimpleStringProperty("99-2137")).
                country(new SimpleStringProperty("Polska")).
                nip(new SimpleStringProperty("PL 123456789")).
                vehicles(new SimpleListProperty<>(vehicles)).
                employees(new SimpleListProperty<>(employees)).build();

        return seller;
    }

    public static Invoice getInvoice() {
        return invoice;
    }
}
