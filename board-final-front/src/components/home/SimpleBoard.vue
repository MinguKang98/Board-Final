<template>
  <div class="container">
    <div>
      {{ boardInfo.title }}
      <RouterLink :to="`/board/${type}`">더보기</RouterLink>
    </div>
    <table class="table">
      <tbody>
      <SimpleBoardItem v-for="(board, index) in boardList" :key="index" :board="board"
                       :type="type"></SimpleBoardItem>
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
    type: String,
    callBoardList: Function
  },
  data() {
    return {
      boardTypeMap: {
        notify: {
          title: '공지사항',
        },
        free: {
          title: '자유게시판',
        },
        member: {
          title: '회원게시판',
        },
        news: {
          title: '뉴스',
        },
      },
      boardList : Array
    }
  },
  computed: {
    boardInfo() {
      return this.boardTypeMap[this.type];
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