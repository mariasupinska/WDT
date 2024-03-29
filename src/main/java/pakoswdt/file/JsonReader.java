package pakoswdt.file;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import pakoswdt.model.InvoiceSummary;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class JsonReader {
    private Gson gson = new Gson();

    public InvoiceSummary getInvoiceSummaryFromFile(File file) {
        String data;
        InvoiceSummary invoiceSummary = new InvoiceSummary();
        try {
            data = FileUtils.readFileToString(file, "CP1250");

            if (StringUtils.isNotEmpty(data)) {
                invoiceSummary = gson.fromJson(data, InvoiceSummary.class);
            }
        } catch (Exception e) {
            log.error("Unhandled exception", e);
        }
        return invoiceSummary;
    }

    public ArrayList<InvoiceSummary> getListOfInvoiceSummariesFrom(File directory, String[] extensions) {
        ArrayList<InvoiceSummary> invoiceSummaries = new ArrayList<>();
        Collection<File> files = FileUtils.listFiles(directory, extensions, false);
        files.stream().filter(file -> file.getName().contains("invSum")).forEach(file -> {
            InvoiceSummary invSum = getInvoiceSummaryFromFile(file);
            invoiceSummaries.add(invSum);
        });

        return invoiceSummaries;
    }
}
