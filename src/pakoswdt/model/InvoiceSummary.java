package pakoswdt.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class InvoiceSummary{
    private BigDecimal netWeight = new BigDecimal(0.0);
    private BigDecimal grossWeight = new BigDecimal(0.0);
    private BigDecimal packagesWeight = new BigDecimal(0.0);
    private BigDecimal packagesAmount = new BigDecimal(0.0);
    private BigDecimal paperWeight = new BigDecimal(0.0);
    private BigDecimal foilWeight = new BigDecimal(0.0);
    private BigDecimal palettesWeight = new BigDecimal(0.0);
    private LocalDate creationDateExt;
    private String numberExt;
}
