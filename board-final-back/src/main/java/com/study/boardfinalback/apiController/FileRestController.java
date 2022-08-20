package com.study.boardfinalback.apiController;

import com.study.boardfinalback.apiDto.files.FileDetailDto;
import com.study.boardfinalback.domain.File;
import com.study.boardfinalback.service.boards.BoardService;
import com.study.boardfinalback.service.FileService;
import com.study.boardfinalback.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File 관련 api 요청 처리하는 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class FileRestController {

    private final FileService fileService;
    private final FileUtils fileUtils;
    private final BoardService boardService;

    /**
     * 입력받은 boardSeq을 가지는 Board의 모든 File을 가져온다
     *
     * @param boardSeq : File을 가져올 Board의 boardSeq
     * @return : 200 with FileDetailDto List
     */
    @GetMapping("/api/boards/{boardSeq}/files")
    public ResponseEntity<List<FileDetailDto>> files(@PathVariable("boardSeq") int boardSeq) {

        boardService.getBoardBySeq(boardSeq);

        List<FileDetailDto> fileDetailDtoList = fileService.getFileListByBoardSeq(boardSeq)
                .stream()
                .map(f -> new FileDetailDto(f))
                .collect(Collectors.toList());

        return new ResponseEntity<>(fileDetailDtoList, HttpStatus.OK);
    }

    /**
     * fileSeq을 가지는 File 다운로드
     *
     * @param fileSeq : 다운로드 할 File의 fileSeq
     * @return : 200 with Resource
     * @throws IOException
     */
    @GetMapping("/api/files/{fileSeq}")
    public ResponseEntity downloadFile(@PathVariable("fileSeq") int fileSeq) throws IOException {

        File file = fileService.getFileBySeq(fileSeq);
        String fileName = new String(file.getOriginName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        Resource fileResource = fileUtils.getFileResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileResource);
    }

}
