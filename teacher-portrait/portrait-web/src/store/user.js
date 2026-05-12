import { defineStore } from 'pinia'
import { login as loginApi } from '../api/auth'
import { setToken, getToken, setUser, getUser, removeToken, removeUser } from '../utils/token'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken() || '',
    userInfo: getUser() || null
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
    role: (state) => state.userInfo?.role || '',
    userName: (state) => state.userInfo?.name || ''
  },
  actions: {
    async login(loginForm) {
      const res = await loginApi(loginForm)
      const { token, userId, workNo, name, college, role } = res.data
      this.token = token
      this.userInfo = { userId, workNo, name, college, role }
      setToken(token)
      setUser(this.userInfo)
    },
    logout() {
      this.token = ''
      this.userInfo = null
      removeToken()
      removeUser()
    }
  }
})