<template>
  <form @submit.prevent="passwordChangeForm">
    <h1>비밀번호 변경</h1>
    <div class="form-group">
      <label for="originPassword" class="form-label">현재 비밀번호</label>
      <input v-model="originPassword" type="password" class="form-control" id="originPassword" placeholder="현재 비밀번호"
             required>
    </div>
    <div class="form-group">
      <label for="newPassword" class="form-label">새 비밀번호</label>
      <input v-model="newPassword" type="password" class="form-control" id="newPassword" placeholder="새 비밀번호" required>
      <span class="invalid-feedback" id="newPasswordWarning"></span>
    </div>
    <div class="form-group">
      <label for="newPasswordCheck" class="form-label">새 비밀번호 확인</label>
      <input v-model="newPasswordCheck" type="password" class="form-control" id="newPasswordCheck"
             placeholder="새 비밀번호 확인" required>
      <span class="invalid-feedback" id="newPasswordCheckWarning"></span>
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
    async passwordChangeForm() {
      if (this.originPassword === '' || this.newPassword === '' || this.newPasswordCheck === '') {
        return
      } else if (this.newPassword !== this.newPasswordCheck) {
        return
      }

      //TODO 새 비밀번호 형식

      try {

        const passwordData = {
          originPassword: this.originPassword,
          newPassword: this.newPassword,
          newPasswordCheck: this.newPasswordCheck,
        }
        await changePassword(this.getUserSeq, passwordData);
        alert("비밀번호가 변경되었습니다!");
        this.initForm();

      } catch (error) {

        const status = error.response.status;
        if (status === 400) {
          alert("잘못된 형식")
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