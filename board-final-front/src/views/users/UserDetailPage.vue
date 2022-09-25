<template>
  <div class="container">
    <UserInfo :user-seq="userSeq"></UserInfo>
  </div>
</template>

<script>
import UserInfo from "@/components/users/UserInfo";

export default {
  name: "UserDetailPage",
  components: {
    UserInfo,
  },
  computed: {
    userSeq() {
      return Number(this.$route.params.userSeq);
    }
  },
  methods: {
    userSeqCheck() {
      if (isNaN(this.userSeq)) {
        this.$router.push(`/`);
      }
    }
  },
  mounted() {
    this.userSeqCheck();
  }
}
</script>

<style scoped>

</style>