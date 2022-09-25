<template>
  <tr>
    <td v-if="isCategory">{{board.categoryName}}</td>

    <td v-if="board.fileExist"><i class="fas fa-paperclip"></i></td>
    <td v-else></td>
    <td>
      <RouterLink :to="`/board/${type}/${board.boardSeq}?${search}`">
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
  props: {
    board : {
      type: Object,
      default: () => ({
        boardSeq: 0,
        dateCreated: '-',
        dateUpdated: '-',
        title: 'NONE',
        visitCount: 0,
        commentCount: 0,
        fileExist: false,
        userSeq: 0,
        userId: 'NONE',
        categoryName: 'NONE'
      })
    },
    type: {
      type: String,
      default: ''
    },
    search: {
      type: URLSearchParams,
    },
    isCategory: {
      type: Boolean,
      default: false
    },
  },
}
</script>

<style scoped>

</style>