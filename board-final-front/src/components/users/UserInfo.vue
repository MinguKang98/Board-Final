<template>

  <ul>
    <li>{{ user.id }}</li>
    <li>{{ user.name }}</li>
    <li>{{ user.email }}</li>
    <li>{{ user.role }}</li>
  </ul>

</template>

<script>
import {getUser} from "@/api/user";

export default {
  name: "UserInfo",
  props: ['userSeq'],
  data() {
    return {
      user: {},
    };
  },
  watch: {
    userSeq() {
        this.getUser();
    }
  },
  methods: {
    async getUser() {
      try {
        const userResponse = await getUser(this.userSeq);

        this.user = userResponse.data;
      } catch (error) {

        console.log(error);
        const status = error.response.status;

        if (status === 404) {
          await this.$router.push('/*');
        }

      }
    }
  },
  mounted() {
    this.getUser(this.userSeq);
  }
}
</script>

<style scoped>

</style>