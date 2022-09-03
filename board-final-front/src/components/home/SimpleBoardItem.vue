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
  props: {
    board : {
      type: Object,
      default: () => ({
        boardSeq: 0,
        categoryName: 'NONE',
        categorySeq: 1,
        commentCount: 0,
        dateCreated: '1970-01-01',
        dateUpdated: '1970-01-01',
        fileExist: false,
        title: 'NONE',
        userId: 'NONE',
        userSeq: 0,
        visitCount: 0,
      })
    },
    url : {
      type: String,
      default: ''
    }
  }
}
</script>

<style scoped>

</style>