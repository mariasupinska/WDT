package pakoswdt.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class Invoice {
    private Seller seller;
    private Buyer buyer;
    private Vehicle transport;
    private String creator;
    private StringProperty number = new SimpleStringProperty("");
    private StringProperty placeOfExtradition = new SimpleStringProperty("Białystok");
    private ObjectProperty<LocalDate> creationDate = new SimpleObjectProperty<>(LocalDate.now());

}
