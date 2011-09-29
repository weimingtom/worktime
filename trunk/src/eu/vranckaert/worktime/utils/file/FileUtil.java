package eu.vranckaert.worktime.utils.file;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * User: DIRK VRANCKAERT
 * Date: 12/09/11
 * Time: 16:55
 */
public class FileUtil {
    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dest).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }
}
