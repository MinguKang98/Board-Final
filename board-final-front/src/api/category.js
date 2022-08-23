import {instance} from "@/api/index";

function getCategoryList() {
    return instance.get('/api/categories');
}

function getCategory(categorySeq) {
    return instance.get(`/api/categories/${categorySeq}`);
}

export {
    getCategoryList,
    getCategory
}

