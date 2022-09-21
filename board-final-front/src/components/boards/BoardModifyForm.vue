<template>
  <form v-on:submit.prevent="modifyBoard" enctype="multipart/form-data">
    <table class="table">
      <tr>
        <th class="table-primary pl-3 ">작성자</th>
        <td>
          <span v-text="board.user"></span>
        </td>
      </tr>
      <tr>
        <th class="table-primary pl-3 ">작성일자</th>
        <td>
          <span v-text="board.dateCreated"></span>
        </td>
      </tr>
      <tr>
        <th class="table-primary pl-3 ">수정일자</th>
        <td>
          <span v-text="board.dateUpdated"></span>
        </td>
      </tr>
      <tr>
        <th class="table-primary pl-3 ">조회수</th>
        <td>
          <span v-text="board.visitCount"></span>
        </td>
      </tr>
      <tr>
        <th class="table-primary pl-3 ">제목</th>
        <td>
          <input v-if="title === undefined" v-bind:value="board.title" v-on:input="title=$event.target.value"
                 class="form-control" type="text" required minlength="3" maxlength="100"/>
          <input v-else v-bind:value="title" v-on:input="title=$event.target.value" class="form-control" type="text"
                 required minlength="3" maxlength="100"/>
          <!--          <input v-model="board.title" class="form-control" type="text" required minlength="3" maxlength="100"/>-->
          <!--          <span v-if="error.title === ''" id="titleWarning"></span>-->
          <!--          <span v-else v-text="error.title" style="color: red" id="titleWarning"></span>-->
        </td>
      </tr>
      <tr>
        <th class="table-primary pl-3 ">내용</th>
        <td>
          <textarea v-if="content === undefined" v-bind:value="board.content" v-on:input="content=$event.target.value"
                    class="form-control" style="height:300px; resize: none;" required
                    minlength="3" maxlength="2000">{{board.content}}</textarea>
          <textarea v-else v-bind:value="content" v-on:input="content=$event.target.value" class="form-control"
                    style="height:300px; resize: none;" required minlength="3" maxlength="2000"></textarea>
          <!--          <span v-if="content.title === ''" id="contentWarning"></span>-->
          <!--          <span v-else v-text="content.title" style="color: red" id="contentWarning"></span>-->
        </td>
      </tr>
      <tr>
        <th class="table-primary pl-3 ">파일첨부</th>
        <td>
          <div v-for="n in 3">
            <div v-if="n <= fileList.length" class="my-2">
              <i class="fas fa-paperclip"></i>&nbsp;<span v-text="fileList[n-1].originName"></span>&nbsp;
              <button v-on:click="downloadFile(fileList[n-1].fileSeq, fileList[n-1].originName)"
                      class="btn btn-secondary"
                      type="button">
                Download
              </button>
              <button v-on:click="deleteFile(fileList[n-1], n-1)" class="btn btn-secondary" type="button">X</button>
            </div>
            <div v-else class="my-2">
              <input v-on:change="addFile" class="form-control w-50" type="file"/>
            </div>
          </div>
        </td>
      </tr>
    </table>

    <div class="row">
      <div class="col text-start">
        <RouterLink :to="`/${type}/${boardSeq}?${search}`" tag="button" class="btn btn-danger btn-lg">취소
        </RouterLink>
      </div>
      <div class="col text-end">
        <button class="btn btn-primary btn-lg" type="submit">수정</button>
      </div>
    </div>

  </form>
</template>

<script>
import {getNotifyBoard, modifyBoard} from "@/api/board";
import {mapGetters} from "vuex";
import {downloadFile, getFileList} from "@/api/file";

export default {
  name: "BoardModifyForm",
  props: {
    boardSeq: {
      type: Number,
      default: 0
    },
    type: {
      type: String,
      default: ''
    },
    search: {
      type: URLSearchParams
    }
  },
  data() {
    return {
      board: {
        boardSeq: 0,
        dateCreated: '-',
        dateUpdated: '-',
        title: 'NONE',
        visitCount: 0,
        commentCount: 0,
        fileExist: false,
        userSeq: 0,
        userId: 'NONE',
        categoryName: 'NONE'
      },
      title: undefined,
      content: undefined,
      fileList: Array,
      addFileList: [],
      deleteFileList: [],
    }
  },
  computed: {
    ...mapGetters('userStore', ['isAuthorized'])
  },
  methods: {
    async getBoard() {
      try {
        const boardResponse = await getNotifyBoard(this.boardSeq);
        this.board = boardResponse.data
      } catch (error) {
        const status = error.response.status;
        if (status === 400) {
          await this.$router.push('/');
        } else if (status === 404) {
          await this.$router.push('/*');
        } else {
          console.log(error);
        }
      }
    },
    async getFileList() {
      try {
        const fileResponse = await getFileList(this.boardSeq);
        this.fileList = fileResponse.data
      } catch (error) {

        const status = error.response.status;
        if (status === 404) {
          await this.$router.push('/');
        } else {
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
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();

      } catch (error) {

        const status = error.response.status;
        if (status === 404) {
          alert("존재하지 않는 파일입니다.");
        } else {
          console.log(error);
        }
      }
    },
    deleteFile(file, index) {
      this.fileList.splice(index, 1);
      this.deleteFileList.push(file.fileSeq);
    },
    addFile(e) {
      this.addFileList.push(e.target.files[0]);
    },
    async modifyBoard() {
      const result = confirm("게시글을 수정하시겠습니까?");
      if (!result) {
        return;
      }
      // TODO valid check

      const formData = new FormData();
      (this.title === undefined) ? formData.append('title', this.board.title) : formData.append('title', this.title);
      (this.content === undefined) ? formData.append('content', this.board.content) : formData.append('content', this.content);
      for (let i = 0; i < this.addFileList.length; i++) {
        formData.append('addFiles', this.addFileList[i]);
      }
      formData.append('deleteFiles', this.deleteFileList)

      try {
        await modifyBoard(this.type, this.boardSeq, formData);
        await this.$router.push(`/${this.type}/${this.boardSeq}?${this.search}`);

      } catch (error) {
        console.log(error);
      }

    }
  },
  mounted() {
    this.getBoard();
    if (!this.isAuthorized(this.board.user)) {
      this.$router.push(`/${this.type}/${this.boardSeq}`);
    }
    this.getFileList();
  }
}
</script>

<style scoped>

</style>