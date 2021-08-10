package pakoswdt.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

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

    public boolean isEmpty() {
        return StringUtils.isAllEmpty(brand.get(), number.get());
    }

    @Override
    public String toString() {
        return brand.getValue() + " " + number.getValue();
    }
}
