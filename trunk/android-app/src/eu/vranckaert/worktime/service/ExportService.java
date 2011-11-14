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
    File exportCsvFile(String filename, List<String> headers, List<String[]> values, CsvSeparator separator);
}
