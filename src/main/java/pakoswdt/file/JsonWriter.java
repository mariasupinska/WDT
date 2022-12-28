package pakoswdt.file;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import pakoswdt.model.Data;
import pakoswdt.model.InvoiceSummary;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

@Slf4j
public class JsonWriter {
    private Gson gson = new Gson();

    public void exportInvoiceSummary(String invoiceNumber, InvoiceSummary invoiceSummary, String directory) {
        invoiceSummary.setCreationDate(Data.getInvoice().getCreationDate().get());
        invoiceSummary.setInvoiceNumber(invoiceNumber);
        String json = gson.toJson(invoiceSummary);
        String systemSeparator = getSystemSeparator();

        try(Writer writer = new FileWriter( directory + systemSeparator + performReplacements(invoiceNumber, systemSeparator) + "_invSum" + ".json")) {
            writer.write(json);
        } catch (Exception e) {
            log.error("Unhandled exception", e);
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
