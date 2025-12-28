import request from '@/utils/request'

export interface CertificateInfo {
  workId: string
  exists: boolean
  [key: string]: any
}

export const getCertificate = (workId: string) => {
  return request.get<CertificateInfo>(`/blockchain/certificate/${workId}`)
}

export const verifyCopyright = (workId: string, fileHash: string) => {
  return request.get<boolean>('/blockchain/verify', {
    params: { workId, fileHash }
  })
}

export const getWorkTransferHistory = (workId: string) => {
  return request.get<any[]>(`/blockchain/transfer-history/${workId}`)
}