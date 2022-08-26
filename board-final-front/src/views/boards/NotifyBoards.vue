<template>
  <div class="container">
    <SearchBar @searchEvent="search"></SearchBar>
    <div class="row">
      <label>총 {{totalBoardCount}}건</label>
    </div>
    <BoardList :board-list="boardList" :url="'/board/notify'" :search="searchParams"></BoardList>
    <PagingBar :cur-page="curPage" :total-board-count="totalBoardCount" @pagingEvent="paging"></PagingBar>
    <div v-if="isAdmin">
      <RouterLink :to="`/notify/write?${searchParams}`" tag="button" class="btn btn-primary btn-lg" style="float:right">
        등록
      </RouterLink>
    </div>
  </div>
</template>

<script>
import BoardList from "@/components/boards/BoardList";
import {getNotifyBoardList} from "@/api/board";
import {mapGetters} from "vuex";
import SearchBar from "@/components/boards/SearchBar";
import PagingBar from "@/components/boards/PagingBar";

export default {
  name: "NotifyBoards",
  components: {PagingBar, SearchBar, BoardList},
  data() {
    return {
      boardList: Array,
      totalBoardCount: Number,
      dateCreatedFrom: (this.$route.query.dateCreatedFrom == null) ? '' : this.$route.query.dateCreatedFrom,
      dateCreatedTo: (this.$route.query.dateCreatedTo == null) ? '' : this.$route.query.dateCreatedTo,
      text: (this.$route.query.text == null) ? '' : this.$route.query.text,
      curPage: (this.$route.query.curPage == null) ? 1 : Number(this.$route.query.curPage),
    }
  },
  computed: {
    ...mapGetters('userStore',["isAdmin"]),
    searchParams() {
      const params = new URLSearchParams();
      params.append('dateCreatedFrom', this.dateCreatedFrom);
      params.append('dateCreatedTo', this.dateCreatedTo);
      params.append('text', this.text);
      params.append('curPage', this.curPage);
      return params;
    }
  },
  watch: {
    searchParams() {
      this.getBoardList();
      this.$router.push({
        path: '/board/notify', query: {
          dateCreatedFrom: this.dateCreatedFrom,
          dateCreatedTo: this.dateCreatedTo,
          text: this.text,
          curPage: this.curPage
        }});
    },
  },
  methods: {
    async getBoardList() {

      try {
        const boardResponse = await getNotifyBoardList(this.searchParams);
        this.boardList = boardResponse.data.boardList;
        this.totalBoardCount = boardResponse.data.totalBoardCount;
      } catch (error) {
        console.log(error);
      }

    },
    search(value) {
      this.dateCreatedFrom = value.dateCreatedFrom;
      this.dateCreatedTo = value.dateCreatedTo;
      this.text = value.text;
      this.curPage = 1;
    },
    paging(value) {
      this.curPage = Number(value);
    },
  },
  mounted() {
    this.getBoardList();
  }
}
</script>

<style scoped>

</style>