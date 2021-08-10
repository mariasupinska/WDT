package pakoswdt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Invoice {
    private Seller seller;
    private Buyer buyer;
    private Vehicle transport;
}
