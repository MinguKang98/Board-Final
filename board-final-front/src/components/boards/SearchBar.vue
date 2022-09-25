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
    <div class="col-5">
      <input v-model="text" class="form-control" type="text" placeholder="검색어를 입력해 주세요. (제목+작성자+내용)"/>
    </div>
    <div class="col-2">
      <button class="btn btn-outline-primary float-right" type="submit">검색</button>
    </div>
  </form>
</template>

<script>
export default {
  name: "SearchBar",
  data() {
    return {
      dateCreatedFrom: "",
      dateCreatedTo: "",
      text: "",
    }
  },
  methods: {
    searchForm() {
      const search = {
        dateCreatedFrom: this.dateCreatedFrom,
        dateCreatedTo: this.dateCreatedTo,
        text: this.text,
      }
      this.$emit("searchEvent", search);
    }
  }
}
</script>

<style scoped>

</style>