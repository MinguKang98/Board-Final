import {instance, instanceWithAuth} from "@/api/index";

function getNotifyBoardList(search) {
    return instance.get('/api/boards/notify', {params: search});
}

function getFreeBoardList(search) {
    return instance.get('/api/boards/free', {params: search});
}

function getMemberBoardList(search) {
    return instance.get('/api/boards/member', {params: search});
}

function getNewsBoardList(search) {
    return instance.get('/api/boards/news', {params: search});
}

function getNotifyBoard(boardSeq) {
    return instance.get(`/api/boards/notify/${boardSeq}`);
}

function getFreeBoard(boardSeq) {
    return instance.get(`/api/boards/free/${boardSeq}`);
}

function getMemberBoard(boardSeq) {
    return instanceWithAuth.get(`/api/boards/member/${boardSeq}`);
}

function getNewsBoard(boardSeq) {
    return instance.get(`/api/boards/news/${boardSeq}`);
}

function writeBoard(type, writeBoardForm) {
    return instanceWithAuth.post(`/api/boards/${type}`, writeBoardForm);
}

function modifyBoard(type, boardSeq, modifyBoardForm) {
    return instanceWithAuth.put(`/api/boards/${type}/${boardSeq}`, modifyBoardForm);
}

function deleteBoard(type, boardSeq) {
    return instanceWithAuth.delete(`/api/boards/${type}/${boardSeq}`);
}

function updateVisitCount(boardSeq) {
    return instance.put(`/api/boards/${boardSeq}/visit-count`);
}

export {
    getNotifyBoardList,
    getFreeBoardList,
    getMemberBoardList,
    getNewsBoardList,
    getNotifyBoard,
    getFreeBoard,
    getMemberBoard,
    getNewsBoard,
    writeBoard,
    modifyBoard,
    deleteBoard,
    updateVisitCount
}