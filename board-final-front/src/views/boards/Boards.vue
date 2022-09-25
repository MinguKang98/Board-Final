<template>
  <div class="container">

    <SearchBarWithCategory v-if="isCategory" @searchEvent="searchWithCategory"></SearchBarWithCategory>
    <SearchBar v-else @searchEvent="search"></SearchBar>

    <div class="row">
      <label>총 {{ totalBoardCount }}건</label>
    </div>

    <BoardList v-if="isCategory" :board-list="boardList" :type="type" :search="searchParamsWithCategory"></BoardList>
    <BoardList v-else :board-list="boardList" :type="type" :search="searchParams"></BoardList>

    <PagingBar :cur-page="curPage" :total-board-count="totalBoardCount" @pagingEvent="paging"></PagingBar>

    <div v-if="isCategory">
      <RouterLink :to="`/${type}/write?${searchParamsWithCategory}`" tag="button" class="btn btn-primary btn-lg"
                  style="float:right">
        등록
      </RouterLink>
    </div>
    <div v-else-if="isAdmin">
      <RouterLink :to="`/${type}/write?${searchParams}`" tag="button" class="btn btn-primary btn-lg"
                  style="float:right">
        등록
      </RouterLink>
    </div>

  </div>
</template>

<script>
import BoardList from "@/components/boards/BoardList";
import {
  getFreeBoardList,
  getMemberBoardList,
  getNewsBoardList,
  getNotifyBoardList
} from "@/api/board";
import {mapGetters} from "vuex";
import SearchBar from "@/components/boards/SearchBar";
import PagingBar from "@/components/boards/PagingBar";
import SearchBarWithCategory from "@/components/boards/SearchBarWithCategory";

export default {
  name: "NotifyBoards",
  components: {SearchBarWithCategory, PagingBar, SearchBar, BoardList},
  data() {
    return {
      boardList: Array,
      totalBoardCount: Number,
      dateCreatedFrom: (this.$route.query.dateCreatedFrom == null) ? '' : this.$route.query.dateCreatedFrom,
      dateCreatedTo: (this.$route.query.dateCreatedTo == null) ? '' : this.$route.query.dateCreatedTo,
      categorySeq: (this.$route.query.categorySeq == null) ? '' : Number(this.$route.query.categorySeq),
      text: (this.$route.query.text == null) ? '' : this.$route.query.text,
      curPage: (this.$route.query.curPage == null) ? 1 : Number(this.$route.query.curPage),
    }
  },
  computed: {
    ...mapGetters('userStore', ["isAdmin"]),
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
  watch: {
    type() {
      this.searchClear();
      this.getBoardList();
      this.$router.push(`/board/${this.type}`).catch(() => {
      });
    },
  },
  methods: {
    async getBoardList() {
      try {
        switch (this.type) {
          case 'notify':
            const notifyBoardResponse = await getNotifyBoardList(this.searchParams);
            this.boardList = notifyBoardResponse.data.boardList;
            this.totalBoardCount = notifyBoardResponse.data.totalBoardCount;
            break;
          case 'free':
            const freeBoardResponse = await getFreeBoardList(this.searchParamsWithCategory);
            this.boardList = freeBoardResponse.data.boardList;
            this.totalBoardCount = freeBoardResponse.data.totalBoardCount;
            break;
          case 'member':
            const memberBoardResponse = await getMemberBoardList(this.searchParamsWithCategory);
            this.boardList = memberBoardResponse.data.boardList;
            this.totalBoardCount = memberBoardResponse.data.totalBoardCount;
            break;
          case 'news':
            const newsBoardResponse = await getNewsBoardList(this.searchParams);
            this.boardList = newsBoardResponse.data.boardList;
            this.totalBoardCount = newsBoardResponse.data.totalBoardCount;
            break;
          default:
            await this.$router.push('/');
        }
      } catch (error) {
        console.log(error);
      }
    },
    search(value) {
      this.dateCreatedFrom = value.dateCreatedFrom;
      this.dateCreatedTo = value.dateCreatedTo;
      this.text = value.text;
      this.curPage = 1;
      this.refresh();
    },
    searchWithCategory(value) {
      this.dateCreatedFrom = value.dateCreatedFrom;
      this.dateCreatedTo = value.dateCreatedTo;
      this.categorySeq = value.categorySeq;
      this.text = value.text;
      this.curPage = 1;
      this.refreshWithCategory();
    },
    paging(value) {
      this.curPage = value;

      if (this.isCategory) {
        this.refreshWithCategory();
      } else {
        this.refresh();
      }
    },
    typeCheck() {
      const types = ['notify', 'free', 'member', 'news'];
      if (types.indexOf(this.type) < 0) {
        this.$router.push('/*');
      }
    },
    searchClear() {
      this.dateCreatedFrom = '';
      this.dateCreatedTo = '';
      this.categorySeq = '';
      this.text = '';
      this.curPage = 1;
    },
    refresh() {
      this.getBoardList();
      this.$router.push({
        path: this.$route.path,
        query: {
          dateCreatedFrom: this.dateCreatedFrom,
          dateCreatedTo: this.dateCreatedTo,
          text: this.text,
          curPage: this.curPage.toString()
        }
      })
    },
    refreshWithCategory() {
      this.getBoardList();
      this.$router.push({
        path: this.$route.path,
        query: {
          dateCreatedFrom: this.dateCreatedFrom,
          dateCreatedTo: this.dateCreatedTo,
          categorySeq: this.categorySeq.toString(),
          text: this.text,
          curPage: this.curPage.toString()
        }
      })
    }
  },
  mounted() {
    this.typeCheck();
    this.getBoardList();
  }
}
</script>

<style scoped>

</style>