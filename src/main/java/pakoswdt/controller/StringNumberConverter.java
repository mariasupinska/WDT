package pakoswdt.controller;

import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.DecimalFormat;

public class StringNumberConverter extends StringConverter<Number> {
    private final int precision;
    public StringNumberConverter (int precision) {
        this.precision = precision;
    }

    @Override
    public String toString(Number object) {
        if ( object == null ) return "";
        DecimalFormat myFormatter = new DecimalFormat("##0.000");
        if ( precision == 0 ) myFormatter.applyPattern("##0");
        if ( precision == 2 ) myFormatter.applyPattern("##0.00");
        if ( precision == 3 ) myFormatter.applyPattern("##0.000");
        String output = myFormatter.format(object);
        return output;
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
