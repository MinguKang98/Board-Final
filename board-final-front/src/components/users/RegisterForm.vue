<template>
  <form @submit.prevent="registerForm" class="container">
    <div class="form-group">
      <label for="registerName" class="form-label">이름</label>
      <input v-model="name" type="text" class="form-control" id="registerName" placeholder="이름" required>
      <span class="invalid-feedback" id="nameWarning"></span>
    </div>
    <div class="form-group">
      <label for="registerId" class="form-label">아이디</label>
      <input v-model="id" type="text" class="form-control" id="registerId" placeholder="아이디" required>
      <span class="invalid-feedback" id="idWarning"></span>
      <button @click="idValidCheck" type="button">중복 확인</button>
    </div>
    <div class="form-group">
      <label for="registerEmail" class="form-label">이메일</label>
      <input v-model="email" type="email" class="form-control" id="registerEmail" placeholder="email" required>
      <span class="invalid-feedback" id="emailWarning"></span>
      <button @click="emailValidCheck" type="button">중복 확인</button>
    </div>
    <div class="form-group">
      <label for="registerPassword" class="form-label">비밀번호</label>
      <input v-model="password" type="password" class="form-control" id="registerPassword" placeholder="비밀번호" required>
      <span class="invalid-feedback" id="passwordWarning"></span>
    </div>
    <div class="form-group">
      <label for="registerPasswordCheck" class="form-label">비밀번호 확인</label>
      <input v-model="passwordCheck" type="password" class="form-control" id="registerPasswordCheck"
             placeholder="비밀번호 확인" required>
      <span class="invalid-feedback" id="passwordCheckWarning"></span>
    </div>
    <button type="submit" class="btn btn-primary">회원 가입</button>
  </form>
</template>

<script>
import {emailDuplicatedCheck, idDuplicatedCheck, register} from "@/api/user";

export default {
  name: "RegisterForm",
  data() {
    return {
      name: "",
      id: "",
      email: "",
      password: "",
      passwordCheck: ""
    }
  },
  methods: {
    async registerForm() {
      try {
        //TODO 유효성 검사
        //TODO 중복 검사
        if (this.password !== this.passwordCheck) {
          alert("비밀번호 불일치")
          return;
        }

        const registerData = {
          name: this.name,
          id: this.id,
          email: this.email,
          password: this.password,
        };
        await register(registerData);
        await this.$router.push('/login');
      } catch (error) {

        const status = error.response.status;
        if (status === 400) {
          alert("잘못된 형식")
        } else if (status === 409) {
          alert("중복")
        } else {
          console.log(error);
        }

      }
    },
    async idValidCheck() {
      try {
        if (this.id === '') {
          return;
        }

        const userId = {
          id: this.id
        }
        await idDuplicatedCheck(userId);
        alert("ok");

      } catch (error) {

        const status = error.response.status;
        if (status === 400) {
          alert("잘못된 형식")
        } else if (status === 409) {
          alert("중복")
        } else {
          console.log(error);
        }

      }
    },
    async emailValidCheck() {
      try {
        if (this.email === '') {
          return;
        }

        const userEmail = {
          email: this.email
        }
        await emailDuplicatedCheck(userEmail);
        alert("ok");

      } catch (error) {

        const status = error.response.status;
        if (status === 400) {
          alert("잘못된 형식")
        } else if (status === 409) {
          alert("중복")
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