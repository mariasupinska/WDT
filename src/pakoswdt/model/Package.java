package pakoswdt.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class Package {
    private SimpleStringProperty type;
    private SimpleDoubleProperty amount;
    private SimpleDoubleProperty weight;
}
