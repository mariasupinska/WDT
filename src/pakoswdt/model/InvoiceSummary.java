package pakoswdt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceSummary{
    private BigDecimal netWeight = new BigDecimal(0.0);
    private BigDecimal packagesWeight = new BigDecimal(0.0);
    private BigDecimal packagesAmount = new BigDecimal(0.0);
    private BigDecimal grossWeight = new BigDecimal(0.0);
    private BigDecimal paperWeight = new BigDecimal(0.0);
    private BigDecimal foilWeight = new BigDecimal(0.0);
    private BigDecimal palettesWeight = new BigDecimal(0.0);

    public InvoiceSummary(List<Product> products, BigDecimal palettes) {
        this.netWeight = netWeightSummary(products);
        this.packagesWeight = packagesWeightSummary(products);
        this.packagesAmount = packagesAmountSummary(products);
        this.grossWeight = grossWeightSummary();
        Map<String, BigDecimal> packagesWeightPerType = countWeight(products);
        this.paperWeight = packagesWeightPerType.getOrDefault("Karton", BigDecimal.ZERO).add(packagesWeightPerType.getOrDefault("Papier", BigDecimal.ZERO));
        this.foilWeight = packagesWeightPerType.getOrDefault("Folia", BigDecimal.ZERO);
        this.palettesWeight = palettes;
    }

    private BigDecimal netWeightSummary(List<Product> products) {
        for (Product p : products) {
            netWeight = netWeight.add(BigDecimal.valueOf(p.getNetWeight().doubleValue()));
        }
        return netWeight;
    }

    private BigDecimal packagesWeightSummary(List<Product> products) {
        Set<Package> productPackages = products
                .stream()
                .filter(product -> product.getProductPackage() != null)
                .map(Product::getProductPackage)
                .collect(Collectors.toSet());

        for (Package productPackage : productPackages) {
            BigDecimal multipliedWeight = BigDecimal.valueOf(productPackage.getWeight().doubleValue()).multiply(BigDecimal.valueOf(productPackage.getAmount().doubleValue()));
            packagesWeight = packagesWeight.add(multipliedWeight);
        }
        return packagesWeight;
    }

    private BigDecimal grossWeightSummary() {
        grossWeight = grossWeight.add(netWeight);
        grossWeight = grossWeight.add(packagesWeight);
        return grossWeight;
    }

    private BigDecimal packagesAmountSummary(List<Product> products) {
        List<BigDecimal> amounts = products.stream()
                .filter(product -> product.getProductPackage() != null)
                .map(Product::getProductPackage)
                .distinct()
                .map(pckg -> BigDecimal.valueOf(pckg.getAmount().doubleValue()))
                .collect(Collectors.toList());

        for(BigDecimal amount : amounts) {
            packagesAmount = packagesAmount.add(amount);
        }

        return packagesAmount;
    }

    private Map<String, BigDecimal> countWeight(List<Product> products) {
        Map<String, BigDecimal> weightPerType = new HashMap<>();
        Set<String> usedMultipackages = new HashSet<>();

        for (Product p : products) {
            if ( !usedMultipackages.contains(p.productPackageType()) ) {
                addWeight(weightPerType, p);
                if ( p.getProductPackage().isMultiPackage() ) usedMultipackages.add(p.productPackageType());
            }
        }

        return weightPerType;
    }

    private void addWeight(Map<String, BigDecimal> weightPerType, Product p) {
        String type = p.getProductPackage().getJustType();
        BigDecimal multipliedWeight = BigDecimal.valueOf(p.getProductPackage().getWeight().get()).multiply(BigDecimal.valueOf(p.getProductPackage().getAmount().get()));
        BigDecimal currentWeightPerType = weightPerType.getOrDefault(type, BigDecimal.ZERO);
        BigDecimal newWeightPerType = currentWeightPerType.add(multipliedWeight);
        weightPerType.put(type, newWeightPerType);
    }
}
