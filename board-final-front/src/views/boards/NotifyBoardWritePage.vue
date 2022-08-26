<template>
  <div class="container">
    <BoardWriteForm :url="'/board/notify'" :search="searchParams" @writeBoardEvent="writeBoardEvent"></BoardWriteForm>
  </div>
</template>

<script>
import BoardWriteForm from "@/components/boards/BoardWriteForm";
import {writeBoard} from "@/api/board";

export default {
  name: "NotifyBoardWritePage",
  components: {BoardWriteForm},
  data() {
    return {
      dateCreatedFrom: (this.$route.query.dateCreatedFrom == null) ? '' : this.$route.query.dateCreatedFrom,
      dateCreatedTo: (this.$route.query.dateCreatedTo == null) ? '' : this.$route.query.dateCreatedTo,
      text: (this.$route.query.text == null) ? '' : this.$route.query.text,
      curPage: (this.$route.query.curPage == null) ? 1 : Number(this.$route.query.curPage),
      formData: {}
    }
  },
  computed: {
    searchParams() {
      const params = new URLSearchParams();
      params.append('dateCreatedFrom', this.dateCreatedFrom);
      params.append('dateCreatedTo', this.dateCreatedTo);
      params.append('text', this.text);
      params.append('curPage', this.curPage.toString());
      return params;

    }
  },
  methods: {
    writeBoardEvent(formData) {
      this.formData = formData;
      this.writeBoard();
    },
    async writeBoard() {
      try {

        const writeResponse = await writeBoard('notify', this.formData);
        const location = writeResponse.headers.location;
        const uri = process.env.VUE_APP_API_URL + '/api/boards/notify/';
        const boardSeq = location.substring(location.indexOf(uri) + uri.length);
        await this.$router.push(`/board/notify/${boardSeq}`);

      } catch (error) {

        const status = error.response.status;
        if (status === 400) {
          alert("잘못된 형식");
        } else {
          console.log(error);
        }

      }
    },
  }
}
</script>

<style scoped>

</style>