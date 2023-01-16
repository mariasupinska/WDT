package pakoswdt.model;

import com.opencsv.bean.AbstractBeanField;
import javafx.beans.property.SimpleDoubleProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

@Slf4j
public class SimpleDoublePropertyConverter extends AbstractBeanField {

    @Override
    protected Object convert(String s) {
        if ( s == null) return null;
        String replaceSpaces = s.replaceAll("\\p{Z}","");
        String replace = replaceSpaces.replace(",", ".");
        if (!NumberUtils.isParsable(replace.trim())) {
            log.error("Couldn't parse the value of " + s);
            return null;
        }
        return new SimpleDoubleProperty(Double.parseDouble(replace.trim()));
    }
}
