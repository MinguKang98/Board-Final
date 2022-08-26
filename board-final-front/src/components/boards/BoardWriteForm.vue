<template>
  <form v-on:submit.prevent="saveBoard" enctype="multipart/form-data">
    <table class="table">
      <tr>
        <th class="table-primary pl-3 ">제목</th>
        <td>
          <input v-model="title" class="form-control" type="text" required minlength="3" maxlength="100"/>
          <span v-if="error.title === ''" id="titleWarning"></span>
          <span v-else v-text="error.title" style="color: red" id="titleWarning"></span>
        </td>
      </tr>
      <tr>
        <th class="table-primary pl-3 ">내용</th>
        <td>
          <textarea v-model="content" class="form-control" style="height:300px; resize: none;" required minlength="3" maxlength="2000"></textarea>
          <span v-if="content.title === ''" id="contentWarning"></span>
          <span v-else v-text="content.title" style="color: red" id="contentWarning"></span>
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
        <RouterLink :to="`${url}?${search}`" tag="button" class="btn btn-danger btn-lg">취소</RouterLink>
      </div>
      <div class="col text-end">
        <button class="btn btn-primary btn-lg" type="submit">저장</button>
      </div>
    </div>

  </form>
</template>

<script>
export default {
  name: "BoardWriteForm",
  props: ['url', 'search'],
  data() {
    return {
      title: '',
      content: '',
      files: [],
      error: {
        title: '',
        content: ''
      }
    }
  },
  methods: {
    selectFile(e) {
      this.files.push(e.target.files[0]);
    },
    saveBoard() {

      //TODO valid check

      const formData = new FormData();
      formData.append('title', this.title);
      formData.append('content', this.content);
      formData.append('categorySeq', 1);
      for (let i = 0; i < this.files.length; i++) {
        formData.append('files', this.files[i]);
      }
      this.$emit("writeBoardEvent", formData);

    },
  }
}
</script>

<style scoped>

</style>