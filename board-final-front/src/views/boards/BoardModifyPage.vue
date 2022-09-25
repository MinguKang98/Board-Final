<template>
  <div class="container">
    <BoardModifyForm v-if="isCategory" :board-seq="boardSeq" :type="type" :search="searchParamsWithCategory" :is-category="isCategory"></BoardModifyForm>
    <BoardModifyForm v-else :board-seq="boardSeq" :type="type" :search="searchParams"></BoardModifyForm>
  </div>
</template>

<script>
import BoardModifyForm from "@/components/boards/BoardModifyForm";
import {mapGetters} from "vuex";

export default {
  name: "NotifyBoardModifyPage",
  components: {BoardModifyForm},
  data() {
    return {
      boardSeq: Number(this.$route.params.boardSeq),
      dateCreatedFrom: (this.$route.query.dateCreatedFrom == null) ? '' : this.$route.query.dateCreatedFrom,
      dateCreatedTo: (this.$route.query.dateCreatedTo == null) ? '' : this.$route.query.dateCreatedTo,
      categorySeq: (this.$route.query.categorySeq == null) ? '' : Number(this.$route.query.categorySeq),
      text: (this.$route.query.text == null) ? '' : this.$route.query.text,
      curPage: (this.$route.query.curPage == null) ? 1 : Number(this.$route.query.curPage),
    }
  },
  computed: {
    ...mapGetters("userStore",["isAdmin"]),
    type() {
      return this.$route.params.type.toString();
    },
    isCategory() {
      return (this.type === 'free') || (this.type === 'member');
    },
    searchParams() {
      const params = new URLSearchParams();
      params.append('dateCreatedFrom', this.dateCreatedFrom);
      params.append('dateCreatedTo', this.dateCreatedTo);
      params.append('text', this.text);
      params.append('curPage', this.curPage.toString());
      return params;
    },
    searchParamsWithCategory() {
      const params = new URLSearchParams();
      params.append('dateCreatedFrom', this.dateCreatedFrom);
      params.append('dateCreatedTo', this.dateCreatedTo);
      params.append('categorySeq', this.categorySeq.toString());
      params.append('text', this.text);
      params.append('curPage', this.curPage.toString());
      return params;
    },
  },
  methods: {
    typeCheck() {
      const types = ['notify', 'free', 'member', 'news'];
      if (types.indexOf(this.type) < 0) {
        this.$router.push('/*');
      }
    },
    boardSeqCheck() {
      if (isNaN(this.boardSeq)) {
        this.$router.push(`/board/${this.type}`);
      }
    }
  },
  mounted() {
    this.typeCheck();
    this.boardSeqCheck();
  }
}
</script>

<style scoped>

</style>