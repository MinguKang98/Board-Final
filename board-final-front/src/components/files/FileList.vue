<template>
  <div class="row mb-5">
    <p v-for="file in fileList" >
      <i class="fas fa-download"></i>
      <a v-on:click="downloadFile(file.fileSeq, file.originName)">{{file.originName}}</a>
    </p>
  </div>
</template>

<script>

import {downloadFile, getFileList} from "@/api/file";

export default {
  name: "FileList",
  props: ['boardSeq'],
  data() {
    return {
      fileList: Array
    }
  },
  methods: {
    async getFileList() {
      try {
        const fileResponse = await getFileList(this.boardSeq);
        this.fileList = fileResponse.data
      } catch (error) {

        const status = error.response.status;
        if (status === 404) {
          await this.$router.push('/');
        }else{
          console.log(error);
        }
      }
    },
    async downloadFile(fileSeq, fileName) {
      try {
        const downloadResponse = await downloadFile(fileSeq);
        const url = window.URL.createObjectURL(new Blob([downloadResponse.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download',fileName);
        document.body.appendChild(link);
        link.click();

      } catch (error) {

        const status = error.response.status;
        if (status === 404) {
          alert("존재하지 않는 파일입니다.");
        }else{
          console.log(error);
        }
      }
    },
  },
  mounted() {
    this.getFileList();
  }
}
</script>

<style scoped>

</style>