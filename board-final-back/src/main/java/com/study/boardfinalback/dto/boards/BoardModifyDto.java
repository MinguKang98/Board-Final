package com.study.boardfinalback.dto.boards;

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

    private List<Integer> deleteFiles = new ArrayList<>();

    public List<MultipartFile> getFiles() {
        return files;
    }

    public List<Integer> getDeleteFiles() {
        return deleteFiles;
    }

}
