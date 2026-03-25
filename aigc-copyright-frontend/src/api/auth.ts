import request from '@/utils/request'
import type { LoginRequest, RegisterRequest, LoginResponse, User, UserLookup } from '@/types/user'

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

export const lookupUserByContact = (keyword: string) => {
  return request.get<UserLookup | null>('/users/lookup', {
    params: { keyword }
  })
}

export const uploadUserAvatar = (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post<{ avatarPath: string; avatarUrl: string }>('/users/avatar', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
