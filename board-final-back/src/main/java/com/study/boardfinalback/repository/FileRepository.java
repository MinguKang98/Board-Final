package com.study.boardfinalback.repository;

import com.study.boardfinalback.domain.Comment;
import com.study.boardfinalback.domain.File;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface FileRepository {

    List<File> getFileListByBoardSeq(int boardSeq);

    Optional<File> getFileBySeq(int fileSeq);

    void addFile(File file);

    void deleteFile(int fileSeq);

}
