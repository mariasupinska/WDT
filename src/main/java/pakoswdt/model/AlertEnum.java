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
    NO_ITEMS_SELECTED(WARNING, "Brak zaznaczenia", "Nie zaznaczono żadnych produktów", "Aby ustawić opakowanie należy najpierw" + '\n' + "wybrać produkty z listy."),

    INVALID_PATH(WARNING, "Nieprawidłowa ścieżka", "Nie znalezniono poprawnej ścieżki", "Proszę wybrać poprawną ścieżkę."),
    NO_FILE_FOUND(WARNING, "Brak pliku", "Nie znaleziono pliku z danymi", "Upewnij się że posiadasz przynajmniej jedną wersję pliku z danymi."),
    NOT_FILLED_FIELDS(WARNING, "Brak danych", "Nie wprowadzono prawidłowych danych", "Proszę wypełnić wszystkie pola."),
    VEHICLE_NOT_CHOSEN(WARNING, "Brak danych", "Nie wybrano żadnego transportu", "Wybierz transport ze strony sprzedawcy lub nabywcy."),
    INCORRECT_FILE_NAME(WARNING, "Nieprawidłowa nazwa", "Podano niepoprawną nazwę pliku", "Nazwa pliku nie może składać się" + '\n' + "wyłącznie z 'białych znaków'."),

    SUCCESSFUL_DATABASE_FILE_GENERATION(INFORMATION, "Informacja o wygenerowaniu", "Pomyślnie wygenerowano nowy plik bazy danych", "Plik wygenerowano w wybranym katalogu."),
    SUCCESSFUL_INVOICE_AND_SUMMARY_GENERATION(INFORMATION, "Informacja o wygenerowaniu", "Pomyślnie wygenerowano fakturę i podsumowanie", "Pliki wygenerowano we wskazanych katalogach."),
    SUCCESSFUL_REPORT_GENERATION(INFORMATION, "Informacja o wygenerowaniu", "Pomyślnie wygenerowano raport", "Plik wygenerowano w wybranym katalogu."),

    DATES_IN_INCORRECT_ORDER(WARNING, "Błędne dane", "Niepoprawna kolejność dat", "'Data od' musi być wcześniejsza lub równa polu 'Data do'."),

    CANNOT_LOAD_FILE(ERROR, "Błąd odczytu", "Nie można odczytać danych.", "Wystąpił błąd podczas próby odczytania pliku."),

    UNKNOWN_ERROR(ERROR, "Błąd w programie", "Niezidentyfikowany błąd", "Przepraszamy, ale wystąpił nieznany błąd." + '\n' + "Prosimy o wysłanie pliku z logami, aby błąd" + '\n' + "mógł zostać zidentyfikowany i naprawiony.");

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
