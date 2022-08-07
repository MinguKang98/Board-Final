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

/**
 * Board 클래스 관련 비즈니스 로직 구현 클래스
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    /**
     * 모든 Board return
     *
     * @return : 모든 Board를 가지는 List
     */
    public List<Board> getBoardList() {

        return boardRepository.getBoardList();
    }

    /**
     * 검색 조건에 해당하는 Board(boardType: FREE) 총 갯수 return
     *
     * @param searchCriteria : 검색 조건
     * @return : 검색 조건 만족하는 Board(boardType: FREE) 총 갯수
     */
    public int getTotalFreeBoardCountBySearchCriteria(SearchCriteria searchCriteria) {

        return boardRepository.getTotalFreeBoardCountBySearchCriteria(searchCriteria);
    }

    /**
     * 검색 조건과 페이징 조건 만족하는 Board(boardType: FREE) return
     *
     * @param searchCriteria : 검색 조건
     * @param pagingCriteria : 페이징 조건
     * @return : 검색 조건과 페이징 조건 만족하는 Board(boardType: FREE)들을 가지는 List
     */
    public List<Board> getFreeBoardListBySearchPagingCriteria(SearchCriteria searchCriteria,
                                                          PagingCriteria pagingCriteria) {

        SearchPagingCriteria searchPagingCriteria = SearchPagingCriteria.builder()
                .dateCreatedFrom(searchCriteria.getDateCreatedFrom())
                .dateCreatedTo(searchCriteria.getDateCreatedTo())
                .categorySeq(searchCriteria.getCategorySeq())
                .text(searchCriteria.getText())
                .firstBoardNum(pagingCriteria.getFirstBoardNum())
                .boardCountPerPage(pagingCriteria.getBoardCountPerPage())
                .build();

        return boardRepository.getFreeBoardListBySearchPagingCriteria(searchPagingCriteria);
    }

    /**
     * 검색 조건에 해당하는 Board(boardType: NOTIFY) 총 갯수 return
     *
     * @param searchCriteria : 검색 조건
     * @return : 검색 조건 만족하는 Board(boardType: NOTIFY) 총 갯수
     */
    public int getTotalNotifyBoardCountBySearchCriteria(SearchCriteria searchCriteria) {

        return boardRepository.getTotalNotifyBoardCountBySearchCriteria(searchCriteria);
    }

    /**
     * 검색 조건과 페이징 조건 만족하는 Board(boardType: NOTIFY) return
     *
     * @param searchCriteria : 검색 조건
     * @param pagingCriteria : 페이징 조건
     * @return : 검색 조건과 페이징 조건 만족하는 Board(boardType: NOTIFY)들을 가지는 List
     */
    public List<Board> getNotifyBoardListBySearchPagingCriteria(SearchCriteria searchCriteria,
                                                              PagingCriteria pagingCriteria) {

        SearchPagingCriteria searchPagingCriteria = SearchPagingCriteria.builder()
                .dateCreatedFrom(searchCriteria.getDateCreatedFrom())
                .dateCreatedTo(searchCriteria.getDateCreatedTo())
                .categorySeq(searchCriteria.getCategorySeq())
                .text(searchCriteria.getText())
                .firstBoardNum(pagingCriteria.getFirstBoardNum())
                .boardCountPerPage(pagingCriteria.getBoardCountPerPage())
                .build();

        return boardRepository.getNotifyBoardListBySearchPagingCriteria(searchPagingCriteria);
    }

    /**
     * 검색 조건에 해당하는 Board(boardType: MEMBER) 총 갯수 return
     *
     * @param searchCriteria : 검색 조건
     * @return : 검색 조건 만족하는 Board(boardType: MEMBER) 총 갯수
     */
    public int getTotalMemberBoardCountBySearchCriteria(SearchCriteria searchCriteria) {

        return boardRepository.getTotalMemberBoardCountBySearchCriteria(searchCriteria);
    }

    /**
     * 검색 조건과 페이징 조건 만족하는 Board(boardType: MEMBER) return
     *
     * @param searchCriteria : 검색 조건
     * @param pagingCriteria : 페이징 조건
     * @return : 검색 조건과 페이징 조건 만족하는 Board(boardType: MEMBER)들을 가지는 List
     */
    public List<Board> getMemberBoardListBySearchPagingCriteria(SearchCriteria searchCriteria,
                                                              PagingCriteria pagingCriteria) {

        SearchPagingCriteria searchPagingCriteria = SearchPagingCriteria.builder()
                .dateCreatedFrom(searchCriteria.getDateCreatedFrom())
                .dateCreatedTo(searchCriteria.getDateCreatedTo())
                .categorySeq(searchCriteria.getCategorySeq())
                .text(searchCriteria.getText())
                .firstBoardNum(pagingCriteria.getFirstBoardNum())
                .boardCountPerPage(pagingCriteria.getBoardCountPerPage())
                .build();

        return boardRepository.getMemberBoardListBySearchPagingCriteria(searchPagingCriteria);
    }

    /**
     * 검색 조건에 해당하는 Board(boardType: NEWS) 총 갯수 return
     *
     * @param searchCriteria : 검색 조건
     * @return : 검색 조건 만족하는 Board(boardType: NEWS) 총 갯수
     */
    public int getTotalNewsBoardCountBySearchCriteria(SearchCriteria searchCriteria) {

        return boardRepository.getTotalNewsBoardCountBySearchCriteria(searchCriteria);
    }

    /**
     * 검색 조건과 페이징 조건 만족하는 Board(boardType: NEWS) return
     *
     * @param searchCriteria : 검색 조건
     * @param pagingCriteria : 페이징 조건
     * @return : 검색 조건과 페이징 조건 만족하는 Board(boardType: NEWS)들을 가지는 List
     */
    public List<Board> getNewsBoardListBySearchPagingCriteria(SearchCriteria searchCriteria,
                                                              PagingCriteria pagingCriteria) {

        SearchPagingCriteria searchPagingCriteria = SearchPagingCriteria.builder()
                .dateCreatedFrom(searchCriteria.getDateCreatedFrom())
                .dateCreatedTo(searchCriteria.getDateCreatedTo())
                .categorySeq(searchCriteria.getCategorySeq())
                .text(searchCriteria.getText())
                .firstBoardNum(pagingCriteria.getFirstBoardNum())
                .boardCountPerPage(pagingCriteria.getBoardCountPerPage())
                .build();

        return boardRepository.getNewsBoardListBySearchPagingCriteria(searchPagingCriteria);
    }

    /**
     * 입력받은 boardSeq 가지는 Board return. 해당 Board 없다면 throw BoardNotFoundException
     *
     * @param boardSeq : return할 Board의 boardSeq
     * @return : 입력받은 boardSeq 가지는 Board, 없다면 throw BoardNotFoundException
     */
    public Board getBoardBySeq(int boardSeq) {

        return boardRepository.getBoardBySeq(boardSeq)
                .orElseThrow(BoardNotFoundException::new);
    }

    /**
     * 입력받은 Board와 File을 추가한다
     *
     * @param board : 추가할 Board의 인스턴스
     * @param fileList : 추가할 File들의 List
     * @return : 추가한 Board의 boardSeq
     */
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

    /**
     * 입력받은 Board를 수정하고, File들을 추가, 삭제한다.
     *
     * @param inputBoard : 변경할 필드를 가지는 Board
     * @param addFileList : 추가할 File List
     * @param deleteFileList : 삭제할 File List
     */
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

        String originTitle = originBoard.getTitle();
        String originContent = originBoard.getContent();
        Boolean originFileExist = originBoard.getFileExist();

        Board newBoard = Board.builder().boardSeq(boardSeq).build();
        if (!originTitle.equals(inputTitle)) {
            newBoard.setTitle(inputTitle);
        }
        if (!originContent.equals(inputContent)) {
            newBoard.setContent(inputContent);
        }
        if (originFileExist != inputFileExist) {
            newBoard.setFileExist(inputFileExist);
        }

        boardRepository.updateBoard(newBoard);
    }

    /**
     * 입력받은 boardSeq 가지는 Board 삭제
     *
     * @param boardSeq : 삭제할 Board의 boardSeq
     */
    @Transactional
    public void deleteBoard(int boardSeq) {

        boardRepository.deleteBoard(boardSeq);
    }

    /**
     * 입력받은 boardSeq 가지는 Board의 visitCount 1 증가
     * 
     * @param boardSeq :  update할 Board의 boardSeq
     */
    @Transactional
    public void updateVisitCount(int boardSeq) {

        boardRepository.updateVisitCount(boardSeq);
    }

}
