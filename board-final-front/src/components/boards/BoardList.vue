<template>
  <table class="table">
    <thead class="table-light">
    <tr>
      <th></th>
      <th>제목</th>
      <th>작성자</th>
      <th>조회수</th>
      <th>등록 일시</th>
      <th>수정 일시</th>
    </tr>
    </thead>
    <tbody>
      <BoardItem v-for="(board, index) in boardList"
                 :key="index" :board="board" :url="url" :search="search"></BoardItem>
    </tbody>
  </table>
</template>

<script>
import BoardItem from "@/components/boards/BoardItem";
export default {
  name: "BoardList",
  components: {BoardItem},
  props: ['boardList','url','search'],
}
</script>

<style scoped>

</style>