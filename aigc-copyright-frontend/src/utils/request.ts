import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

export type ApiResult<T> = {
  data: T
  message?: string
}

type RequestInstance = AxiosInstance & {
  get<T = any>(url: string, config?: AxiosRequestConfig): Promise<ApiResult<T>>
  delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<ApiResult<T>>
  head<T = any>(url: string, config?: AxiosRequestConfig): Promise<ApiResult<T>>
  options<T = any>(url: string, config?: AxiosRequestConfig): Promise<ApiResult<T>>
  post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<ApiResult<T>>
  put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<ApiResult<T>>
  patch<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<ApiResult<T>>
}

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
}) as AxiosInstance

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
;(request.interceptors.response as any).use(
  (response: AxiosResponse<any>) => {
    const { code, message, data } = response.data || {}
    if (code === 200) {
      return { data, message } as ApiResult<any>
    }
    ElMessage.error(message || '请求失败')
    return Promise.reject(new Error(message || '请求失败'))
  },
  (error: any) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('username')
      localStorage.removeItem('walletAddress')
      router.push('/login')
      ElMessage.error('登录已过期，请重新登录')
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default request as RequestInstance
