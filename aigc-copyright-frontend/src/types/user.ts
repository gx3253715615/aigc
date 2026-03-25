export interface User {
  id: number
  username: string
  phone?: string
  email?: string
  avatarPath?: string
  avatarUrl?: string
  isAdmin?: number
  authStatus: 'INIT' | 'AUTH'
  status: 'ENABLE' | 'DISABLE'
  walletAddress?: string
}

export interface UserLookup {
  id: number
  username: string
  phone?: string
  email?: string
}

export interface AdminUser {
  id: number
  username: string
  phone?: string
  email?: string
  isAdmin?: number
  authStatus: 'INIT' | 'AUTH'
  status: 'ENABLE' | 'DISABLE'
}

export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest {
  username: string
  password: string
  phone?: string
  email?: string
}

export interface LoginResponse {
  token: string
  userId: number
  username: string
  walletAddress: string
}
