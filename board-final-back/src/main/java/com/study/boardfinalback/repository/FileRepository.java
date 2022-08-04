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

    List<File> getFileListByBoardSeq(int BoardSeq);

    Optional<File> getFileBySeq(int FileSeq);

    void addFile(File comment);

    void deleteFile(int commentSeq);

}
