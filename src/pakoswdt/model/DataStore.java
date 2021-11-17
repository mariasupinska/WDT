package pakoswdt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class DataStore {
    private List<Buyer> buyers;
    private Map<String, BigDecimal> products = new HashMap<>();
    private Map<String, BigDecimal> packages = new HashMap<>();
    private String defaultInvoiceSummaryPath = "";
}
