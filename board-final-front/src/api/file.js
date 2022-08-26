import {instance, instanceWithAuth} from "@/api/index";

function getFileList(boardSeq) {
    return instance.get(`/api/boards/${boardSeq}/files`);
}

function downloadFile(fileSeq) {
    return instance.get(`/api/files/${fileSeq}`,{responseType: "blob"});
}

export {
    getFileList,
    downloadFile
}