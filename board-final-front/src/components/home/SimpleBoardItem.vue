<template>
  <tr>
    <td v-if="board.fileExist"><i class="fas fa-paperclip"></i></td>
    <td v-else></td>
    <td>
      <RouterLink :to="`${url}/${board.boardSeq}`">
        <span>{{board.title}} ({{board.commentCount}})</span>
      </RouterLink>
    </td>
    <td>
      <RouterLink :to="`/user/${board.userSeq}`">
        <span>{{board.userId}}</span>
      </RouterLink>
    </td>
    <td>{{board.visitCount}}</td>
    <td>{{board.dateCreated}}</td>
  </tr>
</template>

<script>
export default {
  name: "SimpleBoardItem",
  props: ['board', 'url']
}
</script>

<style scoped>

</style>