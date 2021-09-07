package pakoswdt.model.legacy;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter

public class LegacyData {
    private List<LegacyBuyer> buyers;
    private Map<String, BigDecimal> products;
}
