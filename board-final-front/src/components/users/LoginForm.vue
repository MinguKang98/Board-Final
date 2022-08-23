<template>
  <form @submit.prevent="loginForm">
    <div class="mb-3">
      <label for="id" class="form-label">아이디</label>
      <input v-model="id" type="text" class="form-control" placeholder="아이디" required>
    </div>
    <div class="mb-3">
      <label for="password" class="form-label">비밀번호</label>
      <input v-model="password" type="password" class="form-control" placeholder="비밀번호" required>
    </div>
    <button type="submit" class="btn btn-primary">로그인</button>
  </form>
</template>

<script>
export default {
  name: "LoginForm",
  data() {
    return {
      id: '',
      password: '',
    }
  },
  methods: {
    async loginForm() {
      try {

        const loginData = {
          id: this.id,
          password: this.password
        };
        await this.$store.dispatch('userStore/login', loginData);

        this.$router.push('/');

      } catch (error) {

        console.log(error)
        const status = error.response.status;

        if (status === 404) {
          alert("아이디와 비밀번호가 일치하지 않습니다.")
          this.initPassword();
        }

        if (status === 403) {
          alert("차단된 회원입니다.");
          this.initForm();

        }

      }
    },
    initForm() {
      this.id = '';
      this.password = '';
    },
    initPassword() {
      this.password = '';
    },
  }
}
</script>

<style scoped>

</style>