package pakoswdt.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Business {
    protected StringProperty name;
    protected StringProperty street;
    protected StringProperty city;
    protected StringProperty postalCode;
    protected StringProperty country;
    protected StringProperty nip;
    protected ListProperty<Vehicle> vehicles;
}
