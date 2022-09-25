<template>
  <form @submit.prevent="registerForm" class="container">
    <div class="form-group">
      <label for="registerName" class="form-label">이름</label>
      <input v-model="name" type="text" class="form-control" placeholder="이름" minlength="2" maxlength="10" required>
    </div>
    <div class="form-group">
      <label for="registerId" class="form-label">아이디</label>
      <input v-model="id" type="text" class="form-control" placeholder="아이디" minlength="3" maxlength="10" required>
      <button @click="idValidCheck" type="button">중복 확인</button>
    </div>
    <div class="form-group">
      <label for="registerEmail" class="form-label">이메일</label>
      <input v-model="email" type="email" class="form-control" placeholder="email" required>
      <button @click="emailValidCheck" type="button">중복 확인</button>
    </div>
    <div class="form-group">
      <label for="registerPassword" class="form-label">비밀번호</label>
      <input v-model="password" type="password" class="form-control" placeholder="비밀번호" minlength="4" maxlength="16" required>
    </div>
    <div class="form-group">
      <label for="registerPasswordCheck" class="form-label">비밀번호 확인</label>
      <input v-model="passwordCheck" type="password" class="form-control" placeholder="비밀번호 확인" minlength="4" maxlength="16" required>
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
      passwordCheck: "",
      isAvailable: {
        id: false,
        email: false
      }
    }
  },
  methods: {
    validCheck() {
      if (this.name.length < 2 || this.name.length > 10) {
        alert("이름은 2글자 이상 10글자 이하여야 합니다.")
        return false;
      }

      if (this.id.length < 3 || this.id.length > 10) {
        alert("아이디는 3글자 이상 10글자 이하여야 합니다.");
        return false;
      }

      const regExp = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{4,16}$/;
      if (!regExp.test(this.password)) {
        alert("비밀번호는 4글자 이상 16글자 이하의 영문, 숫자, 특수문자의 조합이여야 합니다.");
        return false;
      }

      return true;
    },
    duplicateCheck() {
      return this.isAvailable.id && this.isAvailable.email;

    },
    async registerForm() {
      try {
        if (!this.validCheck()) {
          return;
        }

        if (!this.duplicateCheck()) {
          alert("중복 확인을 하세요!");
          return;
        }

        if (this.password !== this.passwordCheck) {
          alert("비밀번호 불일치");
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
        this.isAvailable.id = true;

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
        this.isAvailable.email = true;

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