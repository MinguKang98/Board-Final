<template>
  <div class="container">
    <UserSettingNav></UserSettingNav>
    <UserDeleteForm></UserDeleteForm>
  </div>
</template>

<script>
import UserDeleteForm from "@/components/users/UserDeleteForm";
import UserSettingNav from "@/components/users/UserSettingNav";

export default {
  name: "UserDeletePage",
  components: {UserSettingNav, UserDeleteForm}
}
</script>

<style scoped>

</style>