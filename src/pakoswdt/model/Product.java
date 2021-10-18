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

    private Package productPackage = new Package();

    private boolean isEmpty() {
        return StringUtils.isAllEmpty(name.get(), unit.get()) && amount == null;
    }

    public boolean shouldBeIgnored() {
        return this.isEmpty() || name.get().trim().equals("Nazwa produktu");
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

    public String generateKeyWithPackage() {
        String name = StringUtils.defaultIfBlank(this.getName().get(), "").trim();
        String unit = StringUtils.defaultIfBlank(this.getUnit().get(), "").replace(".", "").trim();
        String packageType = StringUtils.defaultIfBlank(this.getProductPackage().getType().get(), "").trim();
        return name + " : " + unit + " : " + packageType;
    }

    public SimpleDoubleProperty amount() {
        if ( amount == null ) this.amount = new SimpleDoubleProperty();
        return amount;
    }

    public SimpleDoubleProperty unitWeight() {
        if ( unitWeight == null ) this.unitWeight = new SimpleDoubleProperty();
        return unitWeight;
    }

    public SimpleDoubleProperty netWeight() {
        if ( netWeight == null ) this.netWeight = new SimpleDoubleProperty();
        return netWeight;
    }

    public String productPackageType() {
        return getProductPackage().getType().get();
    }

    public double productPackageAmount() {
        return getProductPackage().amount().get();
    }

    public double productPackageWeight() {
        return getProductPackage().amount().get();
    }
}
