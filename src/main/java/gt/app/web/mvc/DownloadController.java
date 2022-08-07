package gt.app.web.mvc;


import gt.app.domain.ReceivedFile;
import gt.app.modules.file.FileDownloadUtil;
import gt.app.modules.file.FileService;
import gt.app.modules.file.ReceivedFileService;
import gt.app.modules.file.RetrievalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/download")
@RequiredArgsConstructor
public class DownloadController {

    final ReceivedFileService receivedFileService;
    final FileService fileService;

    @GetMapping("/file/{id}")
    //no security check needed
    public void downloadFile(@PathVariable UUID id, HttpServletResponse response) throws IOException {

        Optional<ReceivedFile> fileOpt = receivedFileService.findById(id);

        if (fileOpt.isEmpty()) {
            throw new RetrievalException("File not found", null);
        }

        var receivedFile = fileOpt.get();

        var fileRes = fileService.loadAsResource(receivedFile.getFileGroup(), receivedFile.getStoredName());

        FileDownloadUtil.downloadFile(response, fileRes.getURL(), receivedFile.getOriginalFileName());
    }
}
