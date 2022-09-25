<template>
  <form @submit.prevent="searchForm()" class="row">
    <div class="col-5">
      <div class="row">
        <div class="col-2 align-middle">등록일</div>
        <div class="col-4">
          <input v-model="dateCreatedFrom" class="form-control" type="date"/>
        </div>
        <div class="col-1 align-middle"> ~</div>
        <div class="col-4">
          <input v-model="dateCreatedTo" class="form-control" type="date"/>
        </div>
      </div>
    </div>
    <div class="col-2">
      <select v-model="categorySeq" class="form-select">
        <option value="0" selected>전체 카테고리</option>
        <option v-for="category in categories" v-bind:value="category.categorySeq"> {{ category.name }}</option>
      </select>
    </div>
    <div class="col-4">
      <input v-model="text" class="form-control" type="text" placeholder="검색어를 입력해 주세요. (제목+작성자+내용)"/>
    </div>
    <div class="col-1">
      <button class="btn btn-outline-primary float-right" type="submit">검색</button>
    </div>
  </form>
</template>

<script>
import {getCategoryList} from "@/api/category";

export default {
  name: "SearchBarWithCategory",
  data() {
    return {
      dateCreatedFrom: "",
      dateCreatedTo: "",
      categorySeq: 0,
      text: "",
      categories: []
    }
  },
  methods: {
    searchForm() {
      const search = {
        dateCreatedFrom: this.dateCreatedFrom,
        dateCreatedTo: this.dateCreatedTo,
        categorySeq: this.categorySeq,
        text: this.text,
      }
      this.$emit("searchEvent", search);
    },
    async getCategories() {
      try {
        const categoryResponse = await getCategoryList();
        this.categories = categoryResponse.data.slice(1);
      } catch (error) {
        console.log(error);
      }
    },
  },
  mounted() {
    this.getCategories();
  }
}
</script>

<style scoped>

</style>