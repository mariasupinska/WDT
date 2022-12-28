package pakoswdt.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@Setter
@ToString

public class Package {
    private SimpleStringProperty type = new SimpleStringProperty("");
    private SimpleDoubleProperty amount;
    private SimpleDoubleProperty weight;

    public SimpleDoubleProperty amount() {
        if ( amount == null ) this.amount = new SimpleDoubleProperty();
        return amount;
    }

    public SimpleDoubleProperty weight() {
        if ( weight == null ) this.weight = new SimpleDoubleProperty();
        return weight;
    }

    public boolean isMultiPackage() {
        return type.get().contains(".");
    }

    public String getJustType() {
        if ( !this.isMultiPackage() ) return type.get();
        String[] split = type.get().split(" ");
        String[] shortened = Arrays.copyOf(split, split.length - 1);
        return String.join("", shortened);
    }
}
