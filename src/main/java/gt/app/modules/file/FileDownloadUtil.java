package gt.app.modules.file;

import org.springframework.util.MimeTypeUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class FileDownloadUtil {

    private FileDownloadUtil() {
    }

    public static void downloadFile(HttpServletResponse response, File file, String originalFileName) throws IOException {
        handle(response, file, originalFileName, null);
    }

    public static void downloadFile(HttpServletResponse response, File file, String originalFileName, String mimeType) throws IOException {
        handle(response, file, originalFileName, mimeType);
    }


    private static void handle(HttpServletResponse response, File file, String originalFileName, String mimeType) throws IOException {
        try (var in = new FileInputStream(file)) {

            // get MIME type of the file

            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = MimeTypeUtils.APPLICATION_OCTET_STREAM.getType();
            }

            // set content attributes for the response
            response.setContentType(mimeType);
            response.setContentLength((int) file.length());

            // This will download the file to the user's computer
            response.setHeader("Content-Disposition", "attachment; filename=" + originalFileName);

            in.transferTo(response.getOutputStream());

            response.getOutputStream().flush();
        }
    }

}
