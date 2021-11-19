package pakoswdt.file;

import com.google.gson.Gson;
import pakoswdt.model.InvoiceSummary;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class JsonWriter {
    private Gson gson = new Gson();

    public void exportInvoiceSummary(String invoiceNumber, InvoiceSummary invoiceSummary, String directory) {
        String json = gson.toJson(invoiceSummary);
        String systemSeparator = getSystemSeparator();

        try(Writer writer = new FileWriter( directory + systemSeparator + performReplacements(invoiceNumber, systemSeparator) + "_invSum" + ".json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String performReplacements(String invoiceNumber, String systemSeparator) {
        return invoiceNumber.replaceAll("/", "_").replaceAll(systemSeparator, "_");
    }

    private String getSystemSeparator() {
        String systemSeparator = File.separator;

        if (systemSeparator.equals("\\")) {
            systemSeparator = "\\\\";
        }

        return systemSeparator;
    }
}
