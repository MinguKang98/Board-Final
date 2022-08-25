<template>
  <tr>
    <td v-if="board.fileExist"><i class="fas fa-paperclip"></i></td>
    <td v-else></td>
    <td>
      <RouterLink :to="`${url}/${board.boardSeq}?${search}`">
        <span>{{board.title}} ({{board.commentCount}})</span>
      </RouterLink>
    </td>
    <td v-text="board.userId"></td>
    <td v-text="board.visitCount"></td>
    <td v-text="board.dateCreated"></td>
    <td v-if="board.dateUpdated === null" v-text="'-'"></td>
    <td v-else v-text="board.dateUpdated"></td>
  </tr>
</template>

<script>
export default {
  name: "BoardItem",
  props:['board','url','search']
}
</script>

<style scoped>

</style>