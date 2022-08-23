package com.study.boardfinalback.dto.files;

import com.study.boardfinalback.domain.File;
import lombok.Getter;
import lombok.Setter;

/**
 * 파일 정보 확인 시 사용되는 DTO
 */
@Getter
@Setter
public class FileDetailResponse {

    private int fileSeq;
    private String originName;

    public FileDetailResponse(File file) {
        this.fileSeq = file.getFileSeq();
        this.originName = file.getOriginName();
    }

}
