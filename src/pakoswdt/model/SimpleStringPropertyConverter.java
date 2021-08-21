package pakoswdt.model;

import com.opencsv.bean.AbstractBeanField;
import javafx.beans.property.SimpleStringProperty;

public class SimpleStringPropertyConverter extends AbstractBeanField {
    @Override
    protected Object convert(String s) {
        return new SimpleStringProperty(s);
    }
}
