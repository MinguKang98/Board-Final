import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token:''
  },
  getters: {
      getToken(state) {
          return state.token;
      },
  },
  mutations: {
      setToken(state, token) {
          state.token = token;
      },
  },
  actions: {
  },
  modules: {
  }
})
