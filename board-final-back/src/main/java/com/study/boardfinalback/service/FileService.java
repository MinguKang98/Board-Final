package com.study.boardfinalback.service;

import com.study.boardfinalback.domain.File;
import com.study.boardfinalback.error.files.FileNotFoundException;
import com.study.boardfinalback.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public List<File> getFileListByBoardSeq(int boardSeq) {

        return fileRepository.getFileListByBoardSeq(boardSeq);
    }

    public File getFileBySeq(int fileSeq) {

        return fileRepository.getFileBySeq(fileSeq)
                .orElseThrow(FileNotFoundException::new);
    }

    @Transactional
    public int addFile(File file) {

        fileRepository.addFile(file);

        return file.getFileSeq();
    }

    @Transactional
    public void deleteFile(int fileSeq) {

        fileRepository.deleteFile(fileSeq);
    }

}
