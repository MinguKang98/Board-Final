<template>
  <div class="container">
    <SimpleBoard :type="'notify'" :call-board-list="getNotifyBoards"></SimpleBoard>
    <SimpleBoard :type="'free'" :call-board-list="getFreeBoards"></SimpleBoard>
    <SimpleBoard :type="'member'" :call-board-list="getMemberBoards"></SimpleBoard>
    <SimpleBoard :type="'news'" :call-board-list="getNewsBoards"></SimpleBoard>
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
  methods: {
    async getNotifyBoards() {
      try {
        const notifyBoardResponse = await getNotifyBoardList();

        return  notifyBoardResponse.data.boardList;
      } catch (error) {
        console.log(error);
      }
    },
    async getFreeBoards() {
      try {
        const freeBoardResponse = await getFreeBoardList();

        return freeBoardResponse.data.boardList;
      } catch (error) {
        console.log(error);
      }
    },
    async getMemberBoards() {
      try {
        const memberBoardResponse = await getMemberBoardList();

        return  memberBoardResponse.data.boardList;
      } catch (error) {
        console.log(error);
      }
    },
    async getNewsBoards() {
      try {
        const newsBoardResponse = await getNewsBoardList();

        return  newsBoardResponse.data.boardList;
      } catch (error) {
        console.log(error);
      }
    },
  },
}
</script>
