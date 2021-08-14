package pakoswdt.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter

public class Buyer extends Business implements Comparable<Buyer> {
    private StringProperty deliveryStreet = new SimpleStringProperty("");
    private StringProperty deliveryCity = new SimpleStringProperty("");
    private StringProperty deliveryPostalCode = new SimpleStringProperty("");
    private StringProperty deliveryCountry = new SimpleStringProperty("");
    private StringProperty personRetrieving = new SimpleStringProperty("");
    private StringProperty personConfirming = new SimpleStringProperty("");

    public Buyer(String name, String street, String city, String postalCode, String country, String nip) {
        this.name = new SimpleStringProperty(name);
        this.street = new SimpleStringProperty(street);
        this.city = new SimpleStringProperty(city);
        this.postalCode = new SimpleStringProperty(postalCode);
        this.country = new SimpleStringProperty(country);
        this.nip = new SimpleStringProperty(nip);
        ObservableList<Vehicle> newVehicles = FXCollections.observableArrayList();
        newVehicles.add(new Vehicle(new SimpleStringProperty(""), new SimpleStringProperty("")));
        this.vehicles = new SimpleListProperty<>(newVehicles);
    }

    @Override
    public String toString() {
        return name.getValue();
    }

    @Override
    public int compareTo(Buyer b) {
        return name.get().compareTo(b.getName().get());
    }
}
