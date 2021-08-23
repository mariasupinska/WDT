package pakoswdt.model.legacy;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class LegacyBusiness {
    protected String name = "";
    protected LegacyAddress address;
    protected String nip = "";
    protected List<LegacyVehicle> vehicles;
}
