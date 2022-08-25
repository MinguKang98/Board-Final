<template>
  <div class="container">
    <div>
      {{title}}
      <RouterLink :to="url">더보기</RouterLink>
    </div>
    <table class="table">
      <tbody>
      <SimpleBoardItem v-for="(board, index) in boardList" :key="index" :board="board" :url="url"></SimpleBoardItem>
      </tbody>
    </table>
  </div>
</template>

<script>
import SimpleBoardItem from "@/components/home/SimpleBoardItem";
export default {
  name: "SimpleNotifyBoard",
  components: {SimpleBoardItem},
  props: ['title','url', 'boardList'],

}
</script>

<style scoped>

</style>