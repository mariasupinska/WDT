package pakoswdt.model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

public class Alerts {
    private AlertEnum alertEnum;
    private String[] alertContentParams;
    private Window owner;

    public Alerts(AlertEnum alertEnum, Window owner, String... params) {
        this.alertEnum = alertEnum;
        this.owner = owner;
        this.alertContentParams = params;
    }

    public Optional<ButtonType> display() {
        Alert alert = new Alert(this.alertEnum.getType());
        alert.initOwner(this.owner);
        alert.setTitle(this.alertEnum.getAlertTitle());
        alert.setHeaderText(this.alertEnum.getAlertHeader());
        alert.setContentText(this.alertEnum.getAlertContent());
        return alert.showAndWait();
    }
}
