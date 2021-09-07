package pakoswdt.model;

import com.opencsv.bean.AbstractBeanField;
import javafx.beans.property.SimpleDoubleProperty;
import org.apache.commons.lang3.math.NumberUtils;

public class SimpleDoublePropertyConverter extends AbstractBeanField {
    @Override
    protected Object convert(String s) {
        if ( s == null) return null;
        String replace = s.replace(",", ".");
        if (!NumberUtils.isParsable(replace)) return null;
        return new SimpleDoubleProperty(Double.parseDouble(replace));
    }
}
