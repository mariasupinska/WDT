package pakoswdt.model.legacy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class LegacyAddress {
    protected String street = "";
    protected String postcode = "";
    protected String city = "";
    protected String country = "";
}