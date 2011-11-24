package eu.vranckaert.worktime.service.impl;

import android.os.Environment;
import android.util.Log;
import eu.vranckaert.worktime.constants.Constants;
import eu.vranckaert.worktime.constants.TextConstants;
import eu.vranckaert.worktime.enums.export.CsvSeparator;
import eu.vranckaert.worktime.service.ExportService;
import eu.vranckaert.worktime.utils.string.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Dirk Vranckaert
 *         Date: 5/11/11
 *         Time: 14:46
 */
public class ExportServiceImpl implements ExportService {
    private static final String LOG_TAG = ExportServiceImpl.class.getSimpleName();

    @Override
    public File exportCsvFile(String filename, List<String> headers, List<String[]> values, CsvSeparator separator) {
        Character seperator = separator.getSeperator();
        String emptyValue = "\"\"";

        StringBuilder result = new StringBuilder();

        if (headers != null && headers.size() > 0) {
            for (String header : headers) {
                if (StringUtils.isNotBlank(header)) {
                    result.append("\"" + header + "\"");
                } else {
                    result.append(emptyValue);
                }
                result.append(seperator);
            }
            result.append(TextConstants.NEW_LINE);
        }

        for (String[] valuesRecord : values) {
            for (int i=0; i<valuesRecord.length; i++) {
                String value = valuesRecord[i];
                if (StringUtils.isNotBlank(value)) {
                    result.append("\"" + value + "\"");
                } else {
                    result.append(emptyValue);
                }
                result.append(seperator);
            }

            result.append(TextConstants.NEW_LINE);
        }

        File defaultStorageDirectory = Environment.getExternalStorageDirectory();

        File folder = getDocumentDirectory();

        File file = new File(getDocumentDirectoryPath() +
                filename +
                "." +
                CSV_EXTENSTION
        );

        try {
            boolean fileAlreadyExists = file.createNewFile();
            if(fileAlreadyExists) {
                file.delete();
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(result.toString());
            bw.close();
            fw.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Exception occurred during export...", e);
        }

        return file;
    }

    @Override
    public String getDocumentDirectoryPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator +
            Constants.Export.EXPORT_DIRECTORY +
            File.separator;
        return path;
    }

    @Override
    public File getDocumentDirectory() {
        File file = new File(getDocumentDirectoryPath());
        if (file.exists() && file.isFile()) {
            Log.d(LOG_TAG, "Directory seems to be a file... Deleting it now...");
            file.delete();
        }
        if (!file.exists()) {
            Log.d(LOG_TAG, "Directory does not exist yet! Creating it now!");
            file.mkdir();
        }
        return file;
    }
}
