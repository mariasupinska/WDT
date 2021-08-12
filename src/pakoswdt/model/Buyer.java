package pakoswdt.model;

import lombok.experimental.SuperBuilder;

@SuperBuilder

public class Buyer extends Business {
    @Override
    public String toString() {
        return name.getValue();
    }
}
