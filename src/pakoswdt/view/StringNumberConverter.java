package pakoswdt.view;

import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class StringNumberConverter extends StringConverter<Number> {
    @Override
    public String toString(Number object) {
        if ( object == null ) return "";
        return object.toString();
    }

    @Override
    public Number fromString(String string) {
        if ( string == null ) return null;
        if (StringUtils.isBlank(string) ) return null;
        String replace = string.replace(",", ".");
        if (!NumberUtils.isParsable(replace)) return null;
        return Double.parseDouble(replace);
    }
}
