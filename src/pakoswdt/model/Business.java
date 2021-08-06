package pakoswdt.model;

import javafx.beans.property.StringProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Business {
    protected StringProperty name;
    protected StringProperty street;
    protected StringProperty city;
    protected StringProperty postalCode;
    protected StringProperty country;
    protected StringProperty nip;

}
