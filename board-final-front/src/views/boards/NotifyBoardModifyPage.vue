<template>
  <div class="container">
    <BoardModifyForm :board-seq="boardSeq" :type="'notify'" :search="searchParams"></BoardModifyForm>
  </div>
</template>

<script>
import BoardModifyForm from "@/components/boards/BoardModifyForm";

export default {
  name: "NotifyBoardModifyPage",
  components: {BoardModifyForm},
  data() {
    return {
      boardSeq: Number(this.$route.params.boardSeq),
      dateCreatedFrom: (this.$route.query.dateCreatedFrom == null) ? '' : this.$route.query.dateCreatedFrom,
      dateCreatedTo: (this.$route.query.dateCreatedTo == null) ? '' : this.$route.query.dateCreatedTo,
      text: (this.$route.query.text == null) ? '' : this.$route.query.text,
      curPage: (this.$route.query.curPage == null) ? 1 : Number(this.$route.query.curPage),

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
}
</script>

<style scoped>

</style>