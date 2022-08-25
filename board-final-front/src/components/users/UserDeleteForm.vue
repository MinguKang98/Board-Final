<template>
  <div>
    <h1>대충 경고한다는 뜻</h1>
    <div>
      <label for="check">삭제에 동의합니다.</label>
      <input v-model="agree" type="checkbox" id="check"/>
    </div>
    <button @click="deleteUser">삭제</button>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import {deleteUser} from "@/api/user";

export default {
  name: "UserDeleteForm",
  data() {
    return {
      agree: false
    }
  },
  computed:{
    ...mapGetters('userStore',['getUserSeq'])
  },
  methods: {
    async deleteUser() {
      if (!this.agree) {
        return
      }

      try{
        await deleteUser(this.getUserSeq);
        alert("회원 탈퇴가 완료되었습니다!");
        await this.$store.dispatch('userStore/logout');
        await this.$router.push('/');
      }
      catch (error) {

        const status = error.response.status;
        if (status === 404) {
          alert("존재하지 않는 사용자");
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