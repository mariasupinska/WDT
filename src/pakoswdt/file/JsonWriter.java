package pakoswdt.file;

import com.google.gson.Gson;
import pakoswdt.model.InvoiceSummary;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class JsonWriter {
    private Gson gson = new Gson();

    public void exportInvoiceSummary(String invoiceNumber, InvoiceSummary invoiceSummary) {
        String json = gson.toJson(invoiceSummary);

        try(Writer writer = new FileWriter( invoiceNumber + "_invSum" + ".json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
