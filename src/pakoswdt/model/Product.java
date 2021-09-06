package pakoswdt.model;

import com.opencsv.bean.CsvCustomBindByPosition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@ToString

public class Product {

    @CsvCustomBindByPosition(position = 1, converter = SimpleStringPropertyConverter.class)
    private SimpleStringProperty name;
    @CsvCustomBindByPosition(position = 2, converter = SimpleDoublePropertyConverter.class)
    private SimpleDoubleProperty amount;
    @CsvCustomBindByPosition(position = 3, converter = SimpleStringPropertyConverter.class)
    private SimpleStringProperty unit;

    private SimpleDoubleProperty unitWeight;
    private SimpleDoubleProperty netWeight;

    private boolean isEmpty() {
        return StringUtils.isAllEmpty(name.get(), unit.get()) && amount == null;
    }

    public boolean shouldBeIgnored() {
        return name.get().trim().equals("Nazwa produktu") || this.isEmpty();
    }

    public String generateKey() {
        String name = StringUtils.defaultIfBlank(this.getName().get(), "").trim();
        String unit = StringUtils.defaultIfBlank(this.getUnit().get(), "").replace(".", "").trim();
        return name + " : " + unit;
    }

    public Product enrich(Map<String, BigDecimal> products) {
        BigDecimal unitWeight = products.get(generateKey());
        if(unitWeight != null) {
            this.unitWeight = new SimpleDoubleProperty(unitWeight.doubleValue());
            BigDecimal netWeight = unitWeight.multiply(BigDecimal.valueOf(amount.get()));
            this.netWeight = new SimpleDoubleProperty(netWeight.doubleValue());
        }

        return this;
    }
}
