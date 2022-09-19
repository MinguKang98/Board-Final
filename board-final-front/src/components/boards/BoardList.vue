<template>
  <table class="table">
    <thead class="table-light">
    <tr>
      <th v-if="isCategory">카테고리</th>
      <th></th>
      <th>제목</th>
      <th>작성자</th>
      <th>조회수</th>
      <th>등록 일시</th>
      <th>수정 일시</th>
    </tr>
    </thead>
    <tbody>
    <BoardItem v-for="(board, index) in boardList" :key="index" :board="board"
               :type="type" :search="search" :is-category="isCategory"></BoardItem>
    </tbody>
  </table>
</template>

<script>
import BoardItem from "@/components/boards/BoardItem";

export default {
  name: "BoardList",
  components: {BoardItem},
  props: {
    boardList: {
      type: Array[10],
      default: []
    },
    type: {
      type: String,
      default: ''
    },
    search: {
      type: URLSearchParams,
    }
  },
  computed: {
    isCategory() {
      return (this.type === 'free') || (this.type === 'member');
    },
  }
}
</script>

<style scoped>

</style>