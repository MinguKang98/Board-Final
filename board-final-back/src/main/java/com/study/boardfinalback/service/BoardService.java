package com.study.boardfinalback.service;

import com.study.boardfinalback.domain.Board;
import com.study.boardfinalback.domain.File;
import com.study.boardfinalback.domain.criteria.PagingCriteria;
import com.study.boardfinalback.domain.criteria.SearchCriteria;
import com.study.boardfinalback.domain.criteria.SearchPagingCriteria;
import com.study.boardfinalback.error.BoardNotFoundException;
import com.study.boardfinalback.repository.BoardRepository;
import com.study.boardfinalback.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    public List<Board> getBoardList() {

        return boardRepository.getBoardList();
    }

    public List<Board> getBoardListByCategorySeq(int categorySeq) {

        return boardRepository.getBoardListByCategorySeq(categorySeq);
    }

    public int getTotalBoardCountBySearchCriteria(SearchCriteria searchCriteria) {

        return boardRepository.getTotalBoardCountBySearchCriteria(searchCriteria);
    }

    public List<Board> getBoardListBySearchPagingCriteria(SearchCriteria searchCriteria,
                                                          PagingCriteria pagingCriteria) {

        SearchPagingCriteria searchPagingCriteria = SearchPagingCriteria.builder()
                .dateCreatedFrom(searchCriteria.getDateCreatedFrom())
                .dateCreatedTo(searchCriteria.getDateCreatedTo())
                .categorySeq(searchCriteria.getCategorySeq())
                .text(searchCriteria.getText())
                .firstBoardNum(pagingCriteria.getFirstBoardNum())
                .boardCountPerPage(pagingCriteria.getBoardCountPerPage())
                .build();

        return boardRepository.getBoardListBySearchPagingCriteria(searchPagingCriteria);
    }

    public Board getBoardBySeq(int boardSeq) {

        return boardRepository.getBoardBySeq(boardSeq)
                .orElseThrow(BoardNotFoundException::new);
    }

    @Transactional
    public int addBoard(Board board, List<File> fileList) {

        boardRepository.addBoard(board);
        int boardSeq = board.getBoardSeq();

        for (File file : fileList) {
            file.setBoardSeq(boardSeq);
            fileRepository.addFile(file);
        }
        return boardSeq;
    }

    @Transactional
    public void updateBoard(Board inputBoard, List<File> addFileList, List<File> deleteFileList) {

        for (File file : addFileList) {
            fileRepository.addFile(file);
        }

        for (File file : deleteFileList) {
            fileRepository.deleteFile(file.getFileSeq());
        }

        int boardSeq = inputBoard.getBoardSeq();
        Board originBoard = boardRepository.getBoardBySeq(boardSeq)
                .orElseThrow(BoardNotFoundException::new);

        String inputTitle = inputBoard.getTitle();
        String inputContent = inputBoard.getContent();
        boolean inputFileExist = (fileRepository.getFileListByBoardSeq(boardSeq).size() > 0);

        boardRepository.updateBoard(originBoard.updateBoard(inputTitle, inputContent, inputFileExist));
    }

    @Transactional
    public void deleteBoard(int boardSeq) {

        boardRepository.deleteBoard(boardSeq);
    }

    @Transactional
    public void updateVisitCount(int boardSeq) {

        boardRepository.updateVisitCount(boardSeq);
    }

}
