package eu.vranckaert.worktime.service;

import eu.vranckaert.worktime.enums.export.CsvSeparator;

import java.io.File;
import java.util.List;

/**
 * @author Dirk Vranckaert
 * Date: 5/11/11
 * Time: 14:44
 */
public interface ExportService {
    /**
     * The extension to be used for CSV exports
     */
    public final String CSV_EXTENSTION = "csv";

    /**
     * Export some data to a CSV file. The exported data will be stored locally. The exact path where it's stored
     * can be retrieved using the method {@link eu.vranckaert.worktime.service.ExportService#getDocumentDirectoryPath()}.
     * @param filename The name of the file <b>WITHOUT</b> the extension. Depending on the implementation the extension
     * will be automatically set. If you however specify an extension it will not be overridden but the correct
     * extension will just be added to the filename.
     * @param headers A list of strings with the values to be shown in the headers.
     * @param values A list with string-arrays containing all the values to be printed. No check is executed if the
     * number of values horizontally equals the number of headers you specified. This may be different!
     * @param separator The {@link CsvSeparator} to be used in the file.
     * @return
     */
    File exportCsvFile(String filename, List<String> headers, List<String[]> values, CsvSeparator separator);

    /**
     * Get the full path where documents will be saved.
     * @return The full path where documents are stored.
     */
    String getDocumentDirectoryPath();

    /**
     * Get the file representation where documents will be saved.
     * @return The {@link File} instance pointing to the directory where documents are saved.
     */
    File getDocumentDirectory();
}
