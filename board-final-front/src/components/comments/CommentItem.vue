<template>
  <li>
    <div v-if="editMode">
      <input v-model="content" class="form-control " style="height:80px;" type="text" required
             placeholder="댓글을 입력해 주세요."/>
      <div>
        <button class="btn btn-primary btn" @click="modifyComment">등록</button>
        <button class="btn btn-danger btn" @click="cancelEditMode">취소</button>
      </div>
    </div>
    <div v-else>
      <div>
        <div>
          <RouterLink :to="`/user/${comment.userSeq}`">{{ comment.userId }}</RouterLink>
          <div>{{ comment.dateCreated }}</div>
        </div>

        <div v-if="isAuthorized(comment.userSeq)">
          <a @click="changeEditMode">수정</a>
          <a @click="deleteComment">삭제</a>
        </div>
      </div>
      <div>
        <p>{{ comment.content }}</p>
      </div>
    </div>
  </li>
</template>

<script>
import {mapGetters} from "vuex";
import {deleteComment, modifyComment} from "@/api/comment";

export default {
  name: "CommentItem",
  props: ['comment'],
  data() {
    return {
      editMode: false,
      content: this.comment.content
    }
  },
  computed: {
    ...mapGetters('userStore', ["isAuthorized"])
  },
  watch:{
    comment() {
      this.content = this.comment.content;
    },
  },
  methods: {
    changeEditMode() {
      this.editMode = true;
    },
    cancelEditMode() {
      this.editMode = false;
      this.content = this.comment.content;
    },
    async modifyComment() {
      try {

        const commentData = {
          content: this.content
        }
        await modifyComment(this.comment.commentSeq, commentData);
        this.cancelEditMode();
        this.$emit("mustRefreshComments");



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
    async deleteComment() {

      const result = confirm("댓글을 삭제하시겠습니까?");
      if (result) {
        try {
          await deleteComment(this.comment.commentSeq);
          this.$emit("commentEvent", true);

        } catch (error) {

          const status = error.response.status;
          if (status === 404) {
            await this.$router.push('/');
          } else {
            console.log(error);
          }

        }
      }
    },
  }
}
</script>

<style scoped>

</style>