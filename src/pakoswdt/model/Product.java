package pakoswdt.model;

import com.opencsv.bean.CsvCustomBindByPosition;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString

public class Product {

    @CsvCustomBindByPosition(position = 1, converter = SimpleStringPropertyConverter.class)
    private SimpleStringProperty name;
    @CsvCustomBindByPosition(position = 2, converter = SimpleStringPropertyConverter.class)
    private SimpleStringProperty amount;
    @CsvCustomBindByPosition(position = 3, converter = SimpleStringPropertyConverter.class)
    private SimpleStringProperty unit;

    private boolean isEmpty() {
        return StringUtils.isAllEmpty(name.get(), amount.get(), unit.get());
    }

    public boolean shouldBeIgnored() {
        return name.get().trim().equals("Nazwa produktu") || this.isEmpty();
    }
}
