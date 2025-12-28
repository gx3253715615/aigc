import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User, LoginRequest, RegisterRequest } from '@/types/user'
import { login as apiLogin, register as apiRegister, getUserInfo, realnameAuth as apiRealnameAuth, getRealnameAuth as apiGetRealnameAuth } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const user = ref<User | null>(null)
  const token = ref<string>(localStorage.getItem('token') || '')

  const isLoggedIn = computed(() => !!token.value)

  const login = async (loginData: LoginRequest) => {
    try {
      const response = await apiLogin(loginData)
      token.value = response.data.token
      localStorage.setItem('token', token.value)
      localStorage.setItem('userId', String(response.data.userId))
      localStorage.setItem('username', response.data.username)
      localStorage.setItem('walletAddress', response.data.walletAddress)
      
      // Get user profile
      await checkLoginStatus()
      
      return { success: true, data: response.data }
    } catch (error: any) {
      return { success: false, message: error.message }
    }
  }

  const register = async (registerData: RegisterRequest) => {
    try {
      const response = await apiRegister(registerData)
      return { success: true, data: response.data }
    } catch (error: any) {
      return { success: false, message: error.message }
    }
  }

  const logout = () => {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userId')
    localStorage.removeItem('username')
    localStorage.removeItem('walletAddress')
  }

  const checkLoginStatus = async () => {
    if (token.value) {
      try {
        const response = await getUserInfo()
        user.value = response.data
      } catch (error) {
        logout()
      }
    }
  }

  const updateUser = (userData: Partial<User>) => {
    if (user.value) {
      user.value = { ...user.value, ...userData }
    }
  }

  const realnameAuth = async (realName: string, idNumber: string) => {
    try {
      const response = await apiRealnameAuth({ realName, idNumber })
      // Refresh user info after realname auth
      await checkLoginStatus()
      return { success: true, data: response.data }
    } catch (error: any) {
      return { success: false, message: error.message }
    }
  }

  const getRealnameAuthStatus = async () => {
    try {
      const response = await apiGetRealnameAuth()
      return { success: true, data: response.data }
    } catch (error: any) {
      return { success: false, message: error.message }
    }
  }

  return {
    user,
    token,
    isLoggedIn,
    login,
    register,
    logout,
    checkLoginStatus,
    updateUser,
    realnameAuth,
    getRealnameAuthStatus
  }
})