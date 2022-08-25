<template>
  <div class="container">
    <SimpleBoard :title="'공지사항'" :url="'/board/notify'" :board-list="notifyBoardList"></SimpleBoard>
    <SimpleBoard :title="'자유게시판'" :url="'/board/free'" :board-list="freeBoardList"></SimpleBoard>
    <SimpleBoard :title="'회원게시판'" :url="'/board/member'" :board-list="memberBoardList"></SimpleBoard>
    <SimpleBoard :title="'뉴스'" :url="'/board/news'" :board-list="newsBoardList"></SimpleBoard>
  </div>
</template>

<script>
import SimpleBoard from "@/components/home/SimpleBoard";
import {getFreeBoardList, getMemberBoardList, getNewsBoardList, getNotifyBoardList} from "@/api/board";
export default {
  name: 'HomePage',
  components: {
    SimpleBoard
  },
  data() {
    return {
      notifyBoardList: Array,
      freeBoardList: Array,
      memberBoardList: Array,
      newsBoardList: Array,
    }
  },
  methods: {
    async getNotifyBoards() {
      try {
        const notifyBoardResponse = await getNotifyBoardList();

        this.notifyBoardList = notifyBoardResponse.data.boardList;
      } catch (error) {
        console.log(error);
      }
    },
    async getFreeBoards() {
      try {
        const freeBoardResponse = await getFreeBoardList();

        this.freeBoardList = freeBoardResponse.data.boardList;
      } catch (error) {
        console.log(error);
      }
    },
    async getMemberBoards() {
      try {
        const memberBoardResponse = await getMemberBoardList();

        this.memberBoardList = memberBoardResponse.data.boardList;
      } catch (error) {
        console.log(error);
      }
    },
    async getNewsBoards() {
      try {
        const newsBoardResponse = await getNewsBoardList();

        this.newsBoardList = newsBoardResponse.data.boardList;
      } catch (error) {
        console.log(error);
      }
    },
    getBoards() {
      this.getNotifyBoards();
      this.getFreeBoards();
      this.getMemberBoards();
      this.getNewsBoards();
    },
  },
  mounted() {
    this.getBoards();
  }

}
</script>
