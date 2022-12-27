package pakoswdt.model;

import javafx.beans.property.ListProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter

public class Seller extends Business {
    private ListProperty<String> employees;
}
