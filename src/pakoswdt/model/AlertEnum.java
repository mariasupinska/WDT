package pakoswdt.model;

import javafx.scene.control.Alert;
import lombok.Getter;

import static javafx.scene.control.Alert.AlertType.*;

@Getter

public enum AlertEnum {
    DELETING_VEHICLE(CONFIRMATION, "Potwierdzenie", "Usuwanie pojazdu", "Czy na pewno chcesz usunąć wybrany pojazd?"),
    DELETING_BUYER(CONFIRMATION, "Potwierdzenie", "Usuwanie firmy", "Czy na pewno chcesz usunąć wybraną firmę?"),
    DELETING_PERSON(CONFIRMATION, "Potwierdzenie", "Usuwanie osoby", "Czy na pewno chcesz usunąć wybraną osobę?"),

    NO_VEHICLE_SELECTED(WARNING, "Brak zaznaczenia", "Nie wybrano żadnego pojazdu", "Aby usunąć pojazd należy go najpierw wybrać z listy."),
    NO_BUYER_SELECTED(WARNING, "Brak zaznaczenia", "Nie wybrano żadnej firmy", "Aby usunąć firmę należy ją najpierw wybrać z listy."),
    NO_PERSON_SELECTED(WARNING, "Brak zaznaczenia", "Nie wybrano żadnej osoby", "Aby usunąć osobę należy ją najpierw wybrać z listy."),
    NO_ITEMS_SELECTED(WARNING, "Brak zaznaczenia", "Nie zaznaczono żadnych produktów", "Aby ustawić opakowanie należy najpierw wybrać produkty z listy."),

    INVALID_PATH(WARNING, "Nieprawidłowa ścieżka", "Nie znalezniono poprawnej ścieżki", "Proszę wybrać poprawną ścieżkę."),
    NO_FILE_FOUND(WARNING, "Brak pliku", "Nie znaleziono pliku z danymi", "Upewnij się że posiadasz przynajmniej jedną wersję pliku z danymi."),
    NO_FILLED_FIELDS(WARNING, "Brak danych", "Nie wprowadzono prawidłowych danych", "Proszę wypełnić wszystkie pola."),

    DATES_IN_INCORRECT_ORDER(WARNING, "Błędne dane", "Niepoprawna kolejność dat", "'Data od' musi być wcześniejsza lub równa polu 'Data do'.");

    private Alert.AlertType type;
    private String alertTitle;
    private String alertHeader;
    private String alertContent;



    AlertEnum(Alert.AlertType type, String alertTitle, String alertHeader, String alertContent) {
        this.type = type;
        this.alertTitle = alertTitle;
        this.alertHeader = alertHeader;
        this.alertContent = alertContent;
    }
}
