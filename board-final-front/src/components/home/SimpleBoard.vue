<template>
  <div class="container">
    <div>
      {{ boardInfo.title }}
      <RouterLink :to="boardInfo.url">더보기</RouterLink>
    </div>
    <table class="table">
      <tbody>
      <SimpleBoardItem v-for="(board, index) in boardList" :key="index" :board="board"
                       :url="boardInfo.url"></SimpleBoardItem>
      </tbody>
    </table>
  </div>
</template>

<script>

import SimpleBoardItem from "@/components/home/SimpleBoardItem";

export default {
  name: "SimpleNotifyBoard",
  components: {SimpleBoardItem},
  props: {
    boardType: String,
    callBoardList: Function
  },
  data() {
    return {
      boardTypeMap: {
        notify: {
          title: '공지사항',
          url: '/board/notify'
        },
        free: {
          title: '자유게시판',
          url: '/board/free'
        },
        member: {
          title: '회원게시판',
          url: '/board/member'
        },
        news: {
          title: '뉴스',
          url: '/board/news'
        },
      },
      boardList : Array
    }
  },
  computed: {
    boardInfo() {
      return this.boardTypeMap[this.boardType];
    }
  },
  methods: {
    getData() {
      const promise = this.callBoardList();
      return promise.then((data) => {
        this.boardList = data;
      });
    }
  },
  mounted() {
    this.getData();
  }
}
</script>

<style scoped>

</style>