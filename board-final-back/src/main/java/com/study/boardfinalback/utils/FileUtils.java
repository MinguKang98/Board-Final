package com.study.boardfinalback.utils;

import com.study.boardfinalback.domain.File;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * File 관련 기능 제공하는 클래스
 */
@Component
public class FileUtils {

    @Value("${file.download-location}")
    private String DOWNLOAD_DIR;
    private String SEPARATOR = java.io.File.separator;

    /**
     * MultipartFile List를 File List로 변경 후 return. 비어있는 경우 빈 List를 return
     *
     * @param multipartFileList : MultipartFile List
     * @return : File List
     * @throws IOException
     */
    public List<File> getAddFileList(List<MultipartFile> multipartFileList) throws IOException {

        List<File> fileList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFileList) {
            if (!multipartFile.isEmpty()) {
                String originFileName = multipartFile.getOriginalFilename();
                String fileExtension = FilenameUtils.getExtension(originFileName).toLowerCase();
                String systemFileName = UUID.randomUUID() + "." + fileExtension;

                java.io.File uploadFile = new java.io.File(DOWNLOAD_DIR + SEPARATOR + systemFileName);
                multipartFile.transferTo(uploadFile);

                File file = File.builder()
                        .originName(originFileName)
                        .systemName(systemFileName)
                        .extension(fileExtension)
                        .build();

                fileList.add(file);
            }
        }

        return fileList;
    }

}
