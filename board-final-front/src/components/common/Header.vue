<template>
  <nav class="navbar">
    <div>
      <RouterLink to="/">HOME</RouterLink>
      <template v-if="isLogin">
        <RouterLink :to="`/user/${getUserSeq}`">{{getUserId}}</RouterLink>
        <RouterLink to="/modify">설정</RouterLink>
        <a @click="logout">로그아웃</a>
      </template>
      <template v-else>
        <RouterLink to="/login">로그인</RouterLink>
        <RouterLink to="/register">회원 가입</RouterLink>
      </template>
    </div>
    <div>
      <RouterLink to="/board/notify" >공지사항</RouterLink>
      <RouterLink to="/board/free">자유게시판</RouterLink>
      <RouterLink to="/board/member">회원게시판</RouterLink>
      <RouterLink to="/board/news">뉴스게시판</RouterLink>
    </div>
  </nav>
</template>

<script>
import {mapGetters} from "vuex";

export default {
  name: "Header",
  computed: {
    ...mapGetters('userStore', ['isLogin', 'getUserSeq','getUserId','getUserRole']),
  },
  methods: {
    logout() {
      this.$store.dispatch('userStore/logout');
      this.$router.go();
    },
  }
}
</script>

<style scoped>

</style>