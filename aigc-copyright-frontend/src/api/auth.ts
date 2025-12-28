import request from '@/utils/request'
import type { LoginRequest, RegisterRequest, LoginResponse, User } from '@/types/user'

export const login = (data: LoginRequest) => {
  return request.post<LoginResponse>('/users/login', data)
}

export const register = (data: RegisterRequest) => {
  return request.post<LoginResponse>('/users/register', data)
}

export const getUserInfo = () => {
  return request.get<User>('/users/profile')
}

export const updateUser = (userData: Partial<User>) => {
  return request.put<string>('/users/profile', userData)
}

export const realnameAuth = (data: { realName: string; idNumber: string }) => {
  return request.post<string>('/users/realname-auth', data)
}

export const getRealnameAuth = () => {
  return request.get<any>('/users/realname-auth')
}