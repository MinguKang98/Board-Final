package com.study.boardfinalback.dto.files;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 업로드 할 파일 등록 시 사용되는 DTO
 */
@Getter
@Setter
public class FileDto {

    private List<MultipartFile> files = new ArrayList<>();

    public List<MultipartFile> getFiles() {
        return files;
    }

    public boolean isFileExist() {
        boolean result = false;
        for (MultipartFile f : files) {
            if (!f.isEmpty()) {
                result = true;
                break;
            }
        }
        return result;
    }
}
