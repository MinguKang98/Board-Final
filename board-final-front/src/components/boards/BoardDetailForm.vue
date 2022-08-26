<template>
  <div>
    <div class="row mb-3">
      <RouterLink :to="`/user/${user.userSeq}`" class="col-6">
        {{user.id}}
      </RouterLink>
      <span class="col-3 text-center">{{board.dateCreated}}</span>
      <span v-if="board.dateUpdated == null" class="col-3 text-center">{{'-'}}</span>
      <span v-else class="col-3 text-center">{{board.dateUpdated}}</span>
    </div>
    <div class="row pb-3 border-bottom border-4 ">
      <h2 class="col-8">{{board.title}}</h2>
      <div class="col-4">
        <span v-text="`조회수: ${board.visitCount}`" class="col align-bottom"></span>
        <div v-if="isAuthorized(board.userSeq)">
          <span> 수정 </span>
          <span> 삭제 </span>
        </div>
      </div>
    </div>
    <br>

    <!--본문-->
    <div class="row px-1 py-3 mx-3 mb-3 border border-primary">
      <p v-text="board.content" ></p>
    </div>
  </div>
</template>

<script>
import {mapGetters} from "vuex";

export default {
  name: "BoardDetailForm",
  props: ['board', 'user'],
  computed: {
    ...mapGetters('userStore', ["isAuthorized"])
  }
}
</script>

<style scoped>

</style>