package pakoswdt.model.legacy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pakoswdt.model.Seller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LegacyData {
    private LegacySeller seller;
    private List<LegacyBuyer> buyers;
    private Map<String, BigDecimal> products;
    private Map<String, BigDecimal> packages;
}
