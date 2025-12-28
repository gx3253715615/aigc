import request from '@/utils/request'
import type { CopyrightTransfer, TransferRequest } from '@/types/work'
import type { PageResult } from './works'

export const getTransferHistory = (workId: string, pageNum = 1, pageSize = 10) => {
  return request.get<PageResult<CopyrightTransfer>>('/copyright/transfers', {
    params: { workId, pageNum, pageSize }
  })
}

export const getTransferById = (id: number) => {
  return request.get<CopyrightTransfer>(`/copyright/transfers/${id}`)
}

export const createTransfer = (transferData: TransferRequest) => {
  return request.post<CopyrightTransfer>('/copyright/transfer', transferData)
}