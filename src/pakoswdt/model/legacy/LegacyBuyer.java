package pakoswdt.model.legacy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LegacyBuyer extends LegacyBusiness {
    protected LegacyAddress deliveryAddress;
    protected String personRetrieving = "";
    protected String personConfirming = "";
}