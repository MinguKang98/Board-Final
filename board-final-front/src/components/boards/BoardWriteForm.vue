<template>
  <form v-on:submit.prevent="saveBoard" enctype="multipart/form-data">
    <table class="table">
      <tr v-if="isCategory">
        <th class="table-primary pl-3 ">카테고리</th>
        <td>
          <select v-model="categorySeq" class="form-control">
            <option value="0" selected>카테고리 선택</option>
            <option v-for="category in categories" v-bind:value="category.categorySeq" v-text="category.name"></option>
          </select>
        </td>
      </tr>
      <tr>
        <th class="table-primary pl-3 ">제목</th>
        <td>
          <input v-model="title" class="form-control" type="text" required minlength="3" maxlength="100"/>
        </td>
      </tr>
      <tr>
        <th class="table-primary pl-3 ">내용</th>
        <td>
          <textarea v-model="content" class="form-control" style="height:300px; resize: none;" required minlength="3"
                    maxlength="2000"></textarea>
        </td>
      </tr>
      <tr>
        <th class="table-primary pl-3 ">파일첨부</th>
        <td>
          <div class="my-2">
            <input v-on:change="selectFile" class="form-control w-50" type="file"/>
          </div>
          <div class="my-2">
            <input v-on:change="selectFile" class="form-control w-50" type="file"/>
          </div>
          <div class="my-2">
            <input v-on:change="selectFile" class="form-control w-50" type="file"/>
          </div>
        </td>
      </tr>
    </table>

    <div class="row">
      <div class="col text-start">
        <RouterLink :to="`/board/${type}?${search}`" tag="button" class="btn btn-danger btn-lg">취소</RouterLink>
      </div>
      <div class="col text-end">
        <button class="btn btn-primary btn-lg" type="submit">저장</button>
      </div>
    </div>

  </form>
</template>

<script>
import {getCategoryList} from "@/api/category";

export default {
  name: "BoardWriteForm",
  props: {
    type: {
      type: String,
      default: ''
    },
    search: {
      type: URLSearchParams
    },
    isCategory: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      categorySeq: 0,
      title: '',
      content: '',
      files: [],
      categories: [],
    }
  },
  methods: {
    async getCategories() {
      try {
        const categoryResponse = await getCategoryList();
        this.categories = categoryResponse.data.slice(1);
      } catch (error) {
        console.log(error);
      }
    },
    selectFile(e) {
      this.files.push(e.target.files[0]);
    },
    validCheck() {
      if (this.title.length < 3 || this.title.length > 100) {
        alert("제목은 3글자 이상 100글자 이하여야 합니다.")
        return false;
      }

      if (this.content.length < 3 || this.content.length > 2000) {
        alert("내용은 3글자 이상 2000글자 이하여야 합니다.");
        return false;
      }

      if (this.isCategory && this.categorySeq === 0) {
        alert("카테고리를 선택하세요.")
        return false;
      }

      return true;
    },
    saveBoard() {

      if (!this.validCheck()) {
        return;
      }

      const formData = new FormData();
      formData.append('title', this.title);
      formData.append('content', this.content);
      formData.append('categorySeq', (this.isCategory) ? this.categorySeq : 1);
      for (let i = 0; i < this.files.length; i++) {
        formData.append('files', this.files[i]);
      }
      this.$emit("writeBoardEvent", formData);

    },
  },
  mounted() {
    this.getCategories();
  }
}
</script>

<style scoped>

</style>