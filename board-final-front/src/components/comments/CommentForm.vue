<template>
  <form v-on:submit.prevent="writeComment" class="row pt-2">
    <div v-if="isLogin" class="row">
      <div class="col-8">
        <input v-model="content" class="form-control" style="height:80px;" type="text" required
               placeholder="댓글을 입력해 주세요."/>
      </div>
      <button class="col-2 btn btn-primary btn-lg" type="submit">등록</button>
    </div>
    <div v-else class="row">
      <div class="col-8">
        <input v-model="content" class="form-control" style="height:80px;" type="text" disabled
               placeholder="로그인이 필요합니다."/>
      </div>
      <RouterLink to="/login" tag="button" class="col-2 btn btn-primary btn-lg">로그인</RouterLink>
    </div>
  </form>
</template>

<script>
import {mapGetters} from "vuex";
import {writeComment} from "@/api/comment";

export default {
  name: "CommentForm",
  props: ['boardSeq'],
  data() {
    return {
      content: ""
    }
  },
  computed: {
    ...mapGetters('userStore', ["isLogin"])
  },
  methods: {
    async writeComment() {
      try {

        const commentData = {
          content: this.content
        }
        await writeComment(this.boardSeq,commentData);
        this.$emit("commentEvent", true);
        this.content = "";
      } catch (error) {

        const status = error.response.status;
        if (status === 400) {
          alert("잘못된 형식")
        } else if (status === 401) {
          alert("로그인 필요")
        } else if (status === 404) {
          alert("없는 게시글")
        } else {
          console.log(error);
        }

      }
    },
  }
}
</script>

<style scoped>

</style>