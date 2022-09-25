import {instance, instanceWithAuth} from "@/api/index";

function getCommentList(boardSeq) {
    return instance.get(`/api/boards/${boardSeq}/comments`);
}

function writeComment(boardSeq, commentData) {
    return instanceWithAuth.post(`/api/boards/${boardSeq}/comments`, commentData);
}

function modifyComment(commentSeq, commentData) {
    return instanceWithAuth.put(`/api/comments/${commentSeq}`, commentData);
}

function deleteComment(commentSeq) {
    return instanceWithAuth.delete(`/api/comments/${commentSeq}`);
}

export {
    getCommentList,
    writeComment,
    modifyComment,
    deleteComment
}