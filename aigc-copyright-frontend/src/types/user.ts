export interface User {
  id: number
  username: string
  phone?: string
  email?: string
  authStatus: 'INIT' | 'AUTH'
  status: 'ENABLE' | 'DISABLE'
  walletAddress?: string
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