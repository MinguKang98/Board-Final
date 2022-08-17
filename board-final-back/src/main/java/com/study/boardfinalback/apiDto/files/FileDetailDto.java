package com.study.boardfinalback.apiDto.files;

import com.study.boardfinalback.domain.File;
import lombok.Getter;
import lombok.Setter;

/**
 * 파일 정보 확인 시 사용되는 DTO
 */
@Getter
@Setter
public class FileDetailDto {

    private int fileSeq;
    private String originName;

    public FileDetailDto(File file) {
        this.fileSeq = file.getFileSeq();
        this.originName = file.getOriginName();
    }

}
