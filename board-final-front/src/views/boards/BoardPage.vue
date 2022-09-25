<template>
  <div class="container">
    <BoardDetailForm v-if="isCategory" :board-seq="boardSeq" :type="type"
                     :search="searchParamsWithCategory"></BoardDetailForm>
    <BoardDetailForm v-else :board-seq="boardSeq" :type="type" :search="searchParams"></BoardDetailForm>
    <FileList :board-seq="boardSeq"></FileList>
    <CommentList :board-seq="boardSeq"></CommentList>
    <div>
      <RouterLink v-if="isCategory" :to="`/board/${type}?${searchParamsWithCategory}`" tag="button"
                  class="btn btn-secondary btn-lg">목록
      </RouterLink>
      <RouterLink v-else :to="`/board/${type}?${searchParams}`" tag="button" class="btn btn-secondary btn-lg">목록
      </RouterLink>
    </div>
  </div>
</template>

<script>
import BoardDetailForm from "@/components/boards/BoardDetailForm";
import FileList from "@/components/files/FileList";
import CommentList from "@/components/comments/CommentList";
import {mapGetters} from "vuex";

export default {
  name: "NotifyBoardDetail",
  components: {CommentList, FileList, BoardDetailForm},
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
    ...mapGetters('userStore', ["isLogin"]),
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
      if (this.type === 'member' && !this.isLogin) {
        this.$router.push('/login');
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