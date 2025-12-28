import request from '@/utils/request'
import type { Work } from '@/types/work'

export interface PageResult<T> {
  records: T[]
  pageNumber: number
  pageSize: number
  totalRow: number
}

export const uploadWork = (formData: FormData) => {
  return request.post<Work>('/works/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export const getMyWorks = (pageNum = 1, pageSize = 10) => {
  return request.get<PageResult<Work>>('/works/my', {
    params: { pageNum, pageSize }
  })
}

export const getUserWorks = (userId: number, pageNum = 1, pageSize = 10) => {
  return request.get<PageResult<Work>>(`/works/user/${userId}`, {
    params: { pageNum, pageSize }
  })
}

export const getWorkById = (id: number) => {
  return request.get<Work>(`/works/${id}`)
}

export const certifyWork = (id: number) => {
  return request.post<string>(`/works/${id}/certify`)
}