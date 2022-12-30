package pakoswdt.model;

import com.opencsv.bean.AbstractBeanField;
import javafx.beans.property.SimpleIntegerProperty;

public class SimpleIntegerPropertyConverter extends AbstractBeanField {

    public static final int NON_PRODUCT_CSV_ROW_INDICATOR = -1;

    @Override
    protected Object convert(String s) {
        Integer val;

        try {
            val = Integer.valueOf(s.trim());
        } catch (NumberFormatException ex) {
            val = NON_PRODUCT_CSV_ROW_INDICATOR;
        }

        return new SimpleIntegerProperty(val);
    }
}
