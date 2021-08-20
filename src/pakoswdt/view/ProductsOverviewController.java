package pakoswdt.view;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import javafx.fxml.FXML;
import pakoswdt.MainApp;
import pakoswdt.model.Product;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ProductsOverviewController {
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void ReadCsvFile() throws IOException, CsvException {
        String fileName = "/home/marysia/Downloads/031dar.csv";

        List<Product> products = new CsvToBeanBuilder(new FileReader(fileName))
                .withType(Product.class)
                .build()
                .parse();

        products.forEach(System.out::println);
    }
}
