package com.study.boardfinaltemplate.utils;

import com.study.boardfinaltemplate.domain.File;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.FileInputStream;
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

    /**
     * MultipartFile List를 File List로 변경 후 return. 비어있는 경우 빈 List를 return
     *
     * @param multipartFileList : MultipartFile List
     * @param boardSeq          : File의 boardSeq
     * @return : File List
     * @throws IOException
     */
    public List<File> getAddFileList(List<MultipartFile> multipartFileList, int boardSeq) throws IOException {

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
                        .boardSeq(boardSeq)
                        .build();

                fileList.add(file);
            }
        }

        return fileList;
    }

    /**
     * request에서 받은 fileChecker와 originFileList로 만든 fileChecker를 비교하여, 존재하지 않는 fileSeq을 가지는 File을 삭제하고 List에 담아 return
     *
     * @param originFileList : 기존 File List
     * @param request
     * @return : File List
     */
    public List<File> getDeleteFileList(List<File> originFileList, MultipartHttpServletRequest request) {

        List<File> fileList = new ArrayList<>();
        for (File originFile : originFileList) {
            String fileChecker = "FILE_" + originFile.getFileSeq();
            if (request.getParameter(fileChecker) == null) {
                fileList.add(originFile);

                java.io.File deleteFile = new java.io.File(DOWNLOAD_DIR + SEPARATOR + originFile.getSystemName());
                deleteFile.delete();
            }
        }

        return fileList;
    }

}


