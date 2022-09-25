<template>
  <div class="container">
    <UserSettingNav></UserSettingNav>
    <PasswordChangeForm></PasswordChangeForm>
  </div>
</template>

<script>
import PasswordChangeForm from "@/components/users/PasswordChangeForm";
import UserSettingNav from "@/components/users/UserSettingNav";
export default {
  name: "UserModifyPage",
  components: {UserSettingNav, PasswordChangeForm}
}
</script>

<style scoped>

</style>