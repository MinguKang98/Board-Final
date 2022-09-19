<template>
  <ul class="pagination justify-content-center">
    <li v-on:click="pageChange(1)" class="page-item">
      <a class="page-link">
        <i class="fas fa-chevron-left"></i><i class="fas fa-chevron-left"></i>
      </a>
    </li>
    <li v-on:click="pageChange(prevPage)" class="page-item">
      <a class="page-link">
        <i class="fas fa-chevron-left"></i>
      </a>
    </li>
    <li v-for="page in pages" class="page-item">
      <a v-if="curPage === page" v-text="page" v-on:click="pageChange(page)"  class="page-link active" ></a>
      <a v-else v-text="page" v-on:click="pageChange(page)"  class="page-link" ></a>
    </li>
    <li v-on:click="pageChange(nextPage)" class="page-item">
      <a class="page-link">
        <i class="fas fa-chevron-right"></i>
      </a>
    </li>
    <li v-on:click="pageChange(totalPageCount)" class="page-item">
      <a class="page-link" >
        <i class="fas fa-chevron-right"></i><i class="fas fa-chevron-right"></i>
      </a>
    </li>
  </ul>
</template>

<script>
export default {
  name: "PagingBar",
  props: ['curPage','totalBoardCount'],
  computed: {
    totalPageCount() {
      return Math.floor(this.totalBoardCount / 10) +1;
    },
    firstPage() {
      return Math.floor(this.curPage / 11) * 10 + 1;
    },
    lastPage() {
      let lastPage = this.firstPage + 9
      return lastPage <= this.totalPageCount ? lastPage : this.totalPageCount

    },
    prevPage() {
      return (this.curPage - 10 < 0) ? 1 : this.curPage - 10;
    },
    nextPage() {
      return (this.curPage + 10 > this.totalPageCount) ? this.totalPageCount : this.curPage + 10;
    },
    pages() {
      const list =[];
      for (let i = this.firstPage; i <= this.lastPage; i += 1) {
        list.push(i);
      }
      return list;
    }
  },
  methods: {
    pageChange(pageNum) {
      this.$emit("pagingEvent", pageNum);
    },
  }
}
</script>

<style scoped>

</style>