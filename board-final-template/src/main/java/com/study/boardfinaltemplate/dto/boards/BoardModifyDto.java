package com.study.boardfinaltemplate.dto.boards;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Board 수정 시 사용되는 DTO
 */
@Getter
@Setter
public class BoardModifyDto {

    @Size(min = 3, max = 100, message = "제목은 3글자에서 100글자 사이여야 합니다.")
    private String title;

    @Size(min = 3, max = 2000, message = "본문은 3글자에서 2000글자 사이여야 합니다.")
    private String content;

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
