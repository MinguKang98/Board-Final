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
    <div class="mb-3">
      <span id="loginError"></span>
    </div>
    <button type="submit" class="btn btn-primary">로그인</button>
    <button type="submit" class="btn btn-primary">로그인</button>
  </form>
</template>

<script>
import {login} from "@/api/user";

export default {
  name: "LoginForm",
  data() {
    return {
      id: '',
      password: ''
    }
  },
  methods: {
    async loginForm() {
      try {
        const loginData = {
          id: this.id,
          password: this.password
        };
        console.log(loginData);
        const { data } = await login(loginData);
        console.log(data)
      } catch (error) {
        console.log(error)
      } finally {

      }
    },
  }
}
</script>

<style scoped>

</style>