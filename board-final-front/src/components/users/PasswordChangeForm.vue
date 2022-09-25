<template>
  <form @submit.prevent="passwordChangeForm">
    <h1>비밀번호 변경</h1>
    <div class="form-group">
      <label for="originPassword" class="form-label">현재 비밀번호</label>
      <input v-model="originPassword" type="password" class="form-control" placeholder="현재 비밀번호"
             minlength="4" maxlength="16" required>
    </div>
    <div class="form-group">
      <label for="newPassword" class="form-label">새 비밀번호</label>
      <input v-model="newPassword" type="password" class="form-control" placeholder="새 비밀번호"
             minlength="4" maxlength="16" required>
    </div>
    <div class="form-group">
      <label for="newPasswordCheck" class="form-label">새 비밀번호 확인</label>
      <input v-model="newPasswordCheck" type="password" class="form-control" placeholder="새 비밀번호 확인"
             minlength="4" maxlength="16" required>
    </div>
    <button type="submit" class="btn btn-primary">비밀번호 변경</button>
  </form>
</template>

<script>
import {mapGetters} from "vuex";
import {changePassword} from "@/api/user";

export default {
  name: "PasswordChangeForm",
  data() {
    return {
      originPassword: '',
      newPassword: '',
      newPasswordCheck: ''
    }
  },
  computed: {
    ...mapGetters("userStore", ["getUserSeq"])
  },
  methods: {
    validCheck() {
      if (this.originPassword === '' || this.newPassword === '' || this.newPasswordCheck === '') {
        return false;
      }

      const regExp = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{4,16}$/;
      if (!regExp.test(this.newPassword)) {
        alert("새 비밀번호는 4글자 이상 16글자 이하의 영문, 숫자, 특수문자의 조합이여야 합니다.");
        return false;
      }

      if (this.newPassword !== this.newPasswordCheck) {
        alert("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
        return false;
      }

      return true;
    },
    async passwordChangeForm() {
      if(!this.validCheck()){
        return;
      }
      try {

        const passwordData = {
          originPassword: this.originPassword,
          newPassword: this.newPassword,
          newPasswordCheck: this.newPasswordCheck,
        }
        await changePassword(this.getUserSeq, passwordData);
        alert("비밀번호가 변경되었습니다!");
        this.initForm();
        await this.$store.dispatch('userStore/logout');
        await this.$router.push('/login');

      } catch (error) {

        const status = error.response.status;
        const errorMessageArr = Object.values(error.response.data.errors);
        console.log(error);
        if (status === 400) {
          const errorMessage = errorMessageArr.join('\n');
          alert(errorMessage);
        } else {
          console.log(error);
        }

      }
    },
    initForm() {
      this.originPassword = '';
      this.newPassword = '';
      this.newPasswordCheck = '';
    },
  }
}
</script>

<style scoped>

</style>