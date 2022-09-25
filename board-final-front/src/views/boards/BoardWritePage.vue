<template>
  <div class="container">
    <BoardWriteForm v-if="isCategory" :type="type" :search="searchParamsWithCategory"
                    :is-category="isCategory" @writeBoardEvent="writeBoardEvent"></BoardWriteForm>
    <BoardWriteForm v-else :type="type" :search="searchParams"
                    @writeBoardEvent="writeBoardEvent"></BoardWriteForm>
  </div>
</template>

<script>
import BoardWriteForm from "@/components/boards/BoardWriteForm";
import {writeBoard} from "@/api/board";
import {mapGetters} from "vuex";

export default {
  name: "NotifyBoardWritePage",
  components: {BoardWriteForm},
  data() {
    return {
      dateCreatedFrom: (this.$route.query.dateCreatedFrom == null) ? '' : this.$route.query.dateCreatedFrom,
      dateCreatedTo: (this.$route.query.dateCreatedTo == null) ? '' : this.$route.query.dateCreatedTo,
      categorySeq: (this.$route.query.categorySeq == null) ? '' : Number(this.$route.query.categorySeq),
      text: (this.$route.query.text == null) ? '' : this.$route.query.text,
      curPage: (this.$route.query.curPage == null) ? 1 : Number(this.$route.query.curPage),
      formData: {}
    }
  },
  computed: {
    ...mapGetters("userStore",["isAdmin"]),
    type() {
      return this.$route.params.type.toString();
    },
    isCategory() {
      return (this.type === 'free') || (this.type === 'member');
    },
    searchParams() {
      const params = new URLSearchParams();
      params.append('dateCreatedFrom', this.dateCreatedFrom);
      params.append('dateCreatedTo', this.dateCreatedTo);
      params.append('text', this.text);
      params.append('curPage', this.curPage.toString());
      return params;
    },
    searchParamsWithCategory() {
      const params = new URLSearchParams();
      params.append('dateCreatedFrom', this.dateCreatedFrom);
      params.append('dateCreatedTo', this.dateCreatedTo);
      params.append('categorySeq', this.categorySeq.toString());
      params.append('text', this.text);
      params.append('curPage', this.curPage.toString());
      return params;
    },
  },
  watch: {
    type() {
      this.typeCheck();
    }
  },
  methods: {
    writeBoardEvent(formData) {
      this.formData = formData;
      this.writeBoard();
    },
    async writeBoard() {
      try {

        const writeResponse = await writeBoard(this.type, this.formData);
        const location = writeResponse.headers.location;
        const uri = `${process.env.VUE_APP_API_URL}/api/boards/${this.type}/`;
        const boardSeq = location.substring(location.indexOf(uri) + uri.length);
        await this.$router.push(`/board/${this.type}/${boardSeq}`);

      } catch (error) {
        const status = error.response.status;
        if (status === 400) {
          alert("잘못된 형식");
        } else {
          console.log(error);
        }

      }
    },
    typeCheck() {
      const types = ['notify', 'free', 'member', 'news'];
      if (types.indexOf(this.type) < 0) {
        this.$router.push('/*');
      }
      if ((this.type === 'notify' || this.type === 'news') && !this.isAdmin) {
        this.$router.push('/');
      }
    },
  },
  mounted() {
    this.typeCheck();
  }
}
</script>

<style scoped>

</style>