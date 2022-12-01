package pakoswdt.tableCell;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import pakoswdt.model.Product;
import pakoswdt.view.StringNumberConverter;

public class EditingNumberCell extends TableCell<Product, Number> {
    private TextField textField;

    private final StringNumberConverter converter;

    public EditingNumberCell(int precision) {
        this.converter = new StringNumberConverter(precision);
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(converter.toString(getItem()));
        setGraphic(null);
    }

    @Override
    public void updateItem(Number item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(converter.toString(getNumber()));
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(converter.toString(getNumber()));
                setGraphic(null);
            }

            Product product = this.getTableView().getItems().get(this.getIndex());

            if ( getNumber().doubleValue() < 0.001 && !product.getProductPackage().getType().getValue().equals("Brak")) {
                setTextFill(Color.BLACK);
                setStyle("-fx-background-color: rgba(201, 26, 19, 0.66)");
            } else {
                setTextFill(Color.BLACK);
                setStyle("");
            }
        }
    }

    private void createTextField() {
        textField = new TextField(converter.toString(getNumber()));
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> arg0,
                 Boolean arg1, Boolean arg2) -> {
                    if (!arg2) {
                        commitInsertedValue();
                    }
                });
        textField.setOnAction(event -> {
            commitInsertedValue();
        });
        this.getTableRow().focusedProperty().addListener(
                (ObservableValue<? extends Boolean> arg0,
                 Boolean arg1, Boolean arg2) -> {
                    if (!arg2) {
                        commitInsertedValue();
                    }
                });

    }

    private void commitInsertedValue() {
        String s = textField.getText();
        String value = s.replace(',', '.');
        Double d;
        try {
            d = Double.valueOf(value);
        } catch (Exception e) {
            d = 0.0;
        }
        commitEdit(d);
    }

    private Number getNumber() {
        return getItem() == null ? 0 : getItem();
    }
}