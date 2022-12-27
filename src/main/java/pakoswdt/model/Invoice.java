package pakoswdt.model;

import javafx.beans.property.*;
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
    private ObjectProperty<LocalDate> deliveryDate = new SimpleObjectProperty<>(LocalDate.now());
    private StringProperty palettes = new SimpleStringProperty("");
    private InvoiceSummary summary;
}
