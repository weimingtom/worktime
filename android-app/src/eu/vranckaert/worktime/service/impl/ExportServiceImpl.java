package eu.vranckaert.worktime.service.impl;

import android.content.Context;
import android.util.Log;
import eu.vranckaert.worktime.constants.TextConstants;
import eu.vranckaert.worktime.enums.export.CsvSeparator;
import eu.vranckaert.worktime.service.ExportService;
import eu.vranckaert.worktime.utils.file.FileUtil;
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
    public File exportCsvFile(Context ctx, String filename, List<String> headers, List<String[]> values, CsvSeparator separator) {
        Character separatorChar = separator.getSeperator();
        String emptyValue = "\"\"";

        StringBuilder result = new StringBuilder();

        if (headers != null && headers.size() > 0) {
            for (String header : headers) {
                if (StringUtils.isNotBlank(header)) {
                    result.append("\"" + header + "\"");
                } else {
                    result.append(emptyValue);
                }
                result.append(separatorChar);
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
                result.append(separatorChar);
            }

            result.append(TextConstants.NEW_LINE);
        }

        File file = new File(
                FileUtil.getExportDir(ctx) +
                File.separator +
                filename +
                "." +
                CSV_EXTENSTION
        );
        FileUtil.applyPermissions(file, true, true, false);

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
}
