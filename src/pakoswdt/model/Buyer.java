package pakoswdt.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter

public class Buyer extends Business {
    private StringProperty personRetrieving = new SimpleStringProperty("");
    private StringProperty personConfirming = new SimpleStringProperty("");

    @Override
    public String toString() {
        return name.getValue();
    }
}
