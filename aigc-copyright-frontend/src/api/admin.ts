import request from '@/utils/request'
import type { AdminUser, RegisterRequest } from '@/types/user'
import type { PageResult } from './works'
import type { Work } from '@/types/work'

export const getAdminUsers = (pageNum = 1, pageSize = 10, keyword?: string) => {
  return request.get<PageResult<AdminUser>>('/admin/users', {
    params: { pageNum, pageSize, keyword }
  })
}

export const createAdminUser = (data: RegisterRequest) => {
  return request.post<any>('/admin/users', data)
}

export const updateAdminUserStatus = (id: number, status: 'ENABLE' | 'DISABLE') => {
  return request.put<string>(`/admin/users/${id}/status`, { status })
}

export const getAdminWorks = (pageNum = 1, pageSize = 10, keyword?: string, status?: string) => {
  return request.get<PageResult<Work>>('/works/admin/list', {
    params: { pageNum, pageSize, keyword, status }
  })
}

export const auditWork = (id: number, status: 'PASS' | 'REJECT') => {
  return request.put<string>(`/works/admin/${id}/audit`, null, {
    params: { status }
  })
}
