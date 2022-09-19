<template>
  <div>
    <div class="row mb-3">
      <RouterLink :to="`/user/${user.userSeq}`" class="col-6">
        {{ user.id }}
      </RouterLink>
      <span class="col-3 text-center">{{ board.dateCreated }}</span>
      <span v-if="board.dateUpdated == null" class="col-3 text-center">{{ '-' }}</span>
      <span v-else class="col-3 text-center">{{ board.dateUpdated }}</span>
    </div>
    <div class="row pb-3 border-bottom border-4 ">
      <h2 v-if="isCategory" v-text="`[${category.name}]   ${board.title}`" class="col-8"></h2>
      <h2 v-else class="col-8">{{ board.title }}</h2>
      <div class="col-4">
        <span v-text="`조회수: ${board.visitCount}`" class="col align-bottom"></span>
        <div v-if="isAuthorized(board.userSeq)">
          <RouterLink :to="`/board/${type}/${board.boardSeq}/modify?${search}`">수정</RouterLink>
          <a @click="deleteBoard"> 삭제 </a>
        </div>
      </div>
    </div>
    <br>

    <!--본문-->
    <div class="row px-1 py-3 mx-3 mb-3 border border-primary">
      <p v-text="board.content"></p>
    </div>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import {deleteBoard, getFreeBoard, getMemberBoard, getNewsBoard, getNotifyBoard, updateVisitCount} from "@/api/board";
import {getUser} from "@/api/user";
import {getCategory} from "@/api/category";

export default {
  name: "BoardDetailForm",
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
      }
    }
  },
  computed: {
    ...mapGetters('userStore', ["isAuthorized"]),
    isCategory() {
      return (this.type === 'free') || (this.type === 'member');
    }
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
        await this.getUser();
        if (this.isCategory) {
          await this.getCategory();
        }
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
    async deleteBoard() {
      const result = confirm("게시글을 삭제하시겠습니까?");
      if (result) {
        try {
          await deleteBoard(this.type, this.board.boardSeq);
          await this.$router.push(`${this.url}?${this.search}`);

        } catch (error) {

          const status = error.response.status;
          if (status === 404) {
            await this.$router.push('/');
          } else {
            console.log(error);
          }
        }
      }
    },
    async updateVisitCount() {
      try {
        await updateVisitCount(this.boardSeq);
      } catch (error) {

        const status = error.response.status;
        if (status === 404) {
          await this.$router.push('/*');
        } else {
          console.log(error);
        }

      }
    }
  },
  mounted() {
    this.getBoard();
    this.updateVisitCount();
  }
}
</script>

<style scoped>

</style>