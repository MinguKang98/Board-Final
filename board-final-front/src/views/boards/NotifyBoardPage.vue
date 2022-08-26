<template>
  <div class="container">
    <BoardDetailForm :board="board" :user="user"></BoardDetailForm>
    <FileList :board-seq="boardSeq"></FileList>
    <CommentList :board-seq="boardSeq"></CommentList>
    <div>
      <RouterLink :to="`/board/notify?${searchParams}`"
                  tag="button" class="btn btn-secondary btn-lg">목록</RouterLink>
    </div>
  </div>
</template>

<script>
import BoardDetailForm from "@/components/boards/BoardDetailForm";
import {getNotifyBoard, updateVisitCount} from "@/api/board";
import {getUser} from "@/api/user";
import FileList from "@/components/files/FileList";
import {mapGetters} from "vuex";
import CommentList from "@/components/comments/CommentList";

export default {
  name: "NotifyBoardDetail",
  components: {CommentList, FileList, BoardDetailForm},
  data() {
    return {
      boardSeq: this.$route.params.boardSeq,
      board: {},
      user: {},
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
  methods: {
    async getBoard() {
      try {
        const boardResponse = await getNotifyBoard(this.boardSeq);
        this.board = boardResponse.data
        await this.getUser();
      } catch (error) {

        const status = error.response.status;
        if (status === 400) {
          await this.$router.push('/');
        } else if (status === 404) {
          await this.$router.push('/*');
        }else{
          console.log(error);
        }
      }

    },
    async updateVisitCount() {
      try {
        await updateVisitCount(this.boardSeq);
      } catch (error) {

        const status = error.response.status;
        if (status === 404) {
          await this.$router.push('/*');
        }else{
          console.log(error);
        }

      }
    },
    async getUser() {
      try {
        const userResponse = await getUser(this.board.userSeq);
        this.user = userResponse.data
      } catch (error) {

        const status = error.response.status;
        if (status === 404) {
          await this.$router.push('/*');
        } else{
          console.log(error);
        }
      }

    },
  },
  mounted() {
    this.updateVisitCount()
    this.getBoard();
  }
}
</script>

<style scoped>

</style>