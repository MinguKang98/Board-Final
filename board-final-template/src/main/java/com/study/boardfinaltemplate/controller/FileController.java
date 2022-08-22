package com.study.boardfinaltemplate.controller;

import com.study.boardfinaltemplate.domain.File;
import com.study.boardfinaltemplate.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Value("${file.download-location}")
    private String DOWNLOAD_DIR;
    private String separator = java.io.File.separator;

    /**
     * 파일을 다운로드 한다.
     *
     * @param fileSeq  : 다운로드 할 File의 fileSeq
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/file/download/{fileSeq}")
    public void fileDownload(@PathVariable("fileSeq") int fileSeq,
                             HttpServletResponse response) throws IOException {

        File file = fileService.getFileBySeq(fileSeq);

        String fileName = new String(file.getOriginName().getBytes(StandardCharsets.UTF_8), "iso-8859-1");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        java.io.File downloadFile = new java.io.File(DOWNLOAD_DIR + separator + file.getSystemName());
        FileInputStream fileInputStream = new FileInputStream(downloadFile);
        OutputStream out = response.getOutputStream();

        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = fileInputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

}
