<template>
  <form v-on:submit.prevent="modifyBoard" enctype="multipart/form-data">
    <table class="table">
      <tr v-if="isCategory">
        <th class="table-primary pl-3 ">카테고리</th>
        <td>
          <span v-text="category.name"></span>
        </td>
      </tr>
      <tr>
        <th class="table-primary pl-3 ">작성자</th>
        <td>
          <span v-text="user.name"></span>
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
          <span v-if="board.dateUpdated == null" v-text="'-'"></span>
          <span v-else v-text="board.dateUpdated"></span>
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
        <RouterLink :to="`/board/${type}/${boardSeq}?${search}`" tag="button" class="btn btn-danger btn-lg">취소
        </RouterLink>
      </div>
      <div class="col text-end">
        <button class="btn btn-primary btn-lg" type="submit">수정</button>
      </div>
    </div>

  </form>
</template>

<script>
import {getFreeBoard, getMemberBoard, getNewsBoard, getNotifyBoard, modifyBoard} from "@/api/board";
import {mapGetters} from "vuex";
import {downloadFile, getFileList} from "@/api/file";
import {getUser} from "@/api/user";
import {getCategory} from "@/api/category";

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
    isCategory: {
      type: Boolean,
      default: false
    },
    search: {
      type: URLSearchParams
    }
  },
  data() {
    return {
      board: {
        boardSeq: 0,
        dateCreated: "-",
        dateUpdated: "-",
        title: 'NONE',
        content: 'NONE',
        visitCount: 0,
        commentCount: 0,
        fileExist: false,
        boardType: 'NONE',
        userSeq: 0,
        categorySeq: 1
      },
      user: {
        userSeq: 0,
        dateCreated: '-',
        id: 'NONE',
        name: 'NONE',
        email: 'NONE',
        role: 'ROLE_MEMBER'
      },
      category: {
        categorySeq: 1,
        name: 'NONE'
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
        switch (this.type) {
          case 'notify':
            const notifyBoardResponse = await getNotifyBoard(this.boardSeq);
            this.board = notifyBoardResponse.data
            break;
          case 'free':
            const freeBoardResponse = await getFreeBoard(this.boardSeq);
            this.board = freeBoardResponse.data
            break;
          case 'member':
            const memberBoardResponse = await getMemberBoard(this.boardSeq);
            this.board = memberBoardResponse.data
            break;
          case 'news':
            const newsBoardResponse = await getNewsBoard(this.boardSeq);
            this.board = newsBoardResponse.data
            break;
          default:
            await this.$router.push('/');
        }
        if (!this.isAuthorized(this.board.userSeq)) {
          await this.$router.push(`/board/${this.type}/${this.boardSeq}`);
        }
        await this.getUser();
        if (this.isCategory) {
          await this.getCategory();
        }
        await this.getFileList();
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
    async getUser() {
      try {
        const userResponse = await getUser(this.board.userSeq);
        this.user = userResponse.data
      } catch (error) {

        const status = error.response.status;
        if (status === 404) {
          await this.$router.push('/*');
        } else {
          console.log(error);
        }
      }
    },
    async getCategory() {
      try {
        const categoryResponse = await getCategory(this.board.categorySeq);
        this.category = categoryResponse.data
      } catch (error) {

        const status = error.response.status;
        if (status === 404) {
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
          await this.$router.push('/*');
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
    validCheck() {
      if (this.title.length < 3 || this.title.length > 100) {
        alert("제목은 3글자 이상 100글자 이하여야 합니다.")
        return false;
      }

      if (this.content.length < 3 || this.content.length > 2000) {
        alert("내용은 3글자 이상 2000글자 이하여야 합니다.");
        return false;
      }

      if (this.isCategory && this.categorySeq === 0) {
        alert("카테고리를 선택하세요.")
        return false;
      }

      return true;
    },
    async modifyBoard() {
      const result = confirm("게시글을 수정하시겠습니까?");
      if (!result) {
        return;
      }

      if (!this.validCheck()) {
        return;
      }

      const formData = new FormData();
      (this.title === undefined) ? formData.append('title', this.board.title) : formData.append('title', this.title);
      (this.content === undefined) ? formData.append('content', this.board.content) : formData.append('content', this.content);
      for (let i = 0; i < this.addFileList.length; i++) {
        formData.append('addFiles', this.addFileList[i]);
      }
      formData.append('deleteFiles', this.deleteFileList)

      try {
        await modifyBoard(this.type, this.boardSeq, formData);
        await this.$router.push(`/board/${this.type}/${this.boardSeq}?${this.search}`);

      } catch (error) {
        console.log(error);
      }

    }
  },
  mounted() {
    this.getBoard();
  }
}
</script>

<style scoped>

</style>