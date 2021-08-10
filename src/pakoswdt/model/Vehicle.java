package pakoswdt.model;
        /*
        nowy branch w githubie
        git pull
        zmiana brancha na nowo utowrzony
        commity i push
        merge request
        link do A
         */

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Vehicle {
    private StringProperty brand;
    private StringProperty number;

    public Vehicle(String brand, String number) {
        this.brand = new SimpleStringProperty(brand);
        this.number = new SimpleStringProperty(number);
    }
    @Override
    public String toString() {
        return brand.getValue() + " " + number.getValue();
    }
}
