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

    private static Seller createSeller() {
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
        vehicles.add(new Vehicle(new SimpleStringProperty(""), new SimpleStringProperty("")));
        vehicles.add(new Vehicle(new SimpleStringProperty("Mercedes"), new SimpleStringProperty("BI 0331F")));
        vehicles.add(new Vehicle(new SimpleStringProperty("Renault"), new SimpleStringProperty("BI 127AP")));

        Seller seller = Seller.builder().name(new SimpleStringProperty("INTERPAKOS Zbigniew Supiński")).
                street(new SimpleStringProperty("Handlowa 3")).
                city(new SimpleStringProperty("Białystok")).
                postalCode(new SimpleStringProperty("15-399")).
                country(new SimpleStringProperty("Polska")).
                nip(new SimpleStringProperty("PL 9660631964")).
                vehicles(new SimpleListProperty<>(vehicles)).build();

        return seller;
    }

    public static Invoice getInvoice() {
        return invoice;
    }
}
