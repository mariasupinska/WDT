package pakoswdt.model;

import javafx.beans.property.SimpleStringProperty;

public class Data {
    public static final Business seller = Seller.builder().name(new SimpleStringProperty("INTERPAKOS Zbigniew Supiński")).
            street(new SimpleStringProperty("Handlowa 3")).
            city(new SimpleStringProperty("Białystok")).
            postalCode(new SimpleStringProperty("15-399")).
            country(new SimpleStringProperty("Polska")).
            nip(new SimpleStringProperty("PL 9660631964")).build();

    public Data() {
        //seller = Seller.builder().street(new SimpleStringProperty("Handlowa 3")).build();
    }
}
