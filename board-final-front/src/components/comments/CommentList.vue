<template>
  <div>
    <div>
      <CommentForm :board-seq="boardSeq" @commentEvent="commentEvent"></CommentForm>
    </div>
    <div>
      <ul>
        <CommentItem v-for="(comment, index) in commentList" :key="index" :comment="comment"
        @commentEvent="commentEvent"></CommentItem>
      </ul>
    </div>
  </div>

</template>

<script>
import CommentForm from "@/components/comments/CommentForm";
import {getCommentList} from "@/api/comment";
import CommentItem from "@/components/comments/CommentItem";

export default {
  name: "CommentList",
  components: {CommentItem, CommentForm},
  props: ['boardSeq'],
  data() {
    return {
      commentList: Array
    }
  },
  methods: {
    async getComments() {
      try {
        const commentResponse = await getCommentList(this.boardSeq);
        this.commentList = commentResponse.data
      } catch (error) {

        const status = error.response.status;
        if (status === 404) {
          await this.$router.push('/');
        }else{
          console.log(error);
        }
      }
    },
    commentEvent() {
      this.getComments();
    },
  },
  mounted() {
    this.getComments();
  }

}
</script>

<style scoped>

</style>