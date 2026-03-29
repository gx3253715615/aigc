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

export interface BlockchainWorkInfo {
  workId: string
  fileHash: string
  author: string
  owner: string
  certifyTime: string
}

export const getWorkFromBlockchain = (workId: string) => {
  return request.get<BlockchainWorkInfo>(`/works/blockchain/${workId}`)
}

export const getTransferHistoryByWorkId = (workId: string) => {
  return request.get<string[]>(`/copyright/blockchain/transfersHistory/${workId}`)
}

export const getBlockchainTransferDetail = (transferId: string) => {
  return request.get<any>(`/copyright/blockchain/transfers/${transferId}`)
}