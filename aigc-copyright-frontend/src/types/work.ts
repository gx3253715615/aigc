export interface Work {
  id: number
  workId: string
  userId: number
  userName?: string
  fileName: string
  fileType: 'TEXT' | 'IMAGE' | 'AUDIO' | 'VIDEO' | 'OTHER'
  fileSize: number
  fileHash: string
  summary?: string
  modelName?: string
  modelVersion?: string
  modelSource?: string
  modelParams?: Record<string, any>
  prompt?: string
  creationType?: string
  workStatus: 'INIT' | 'UPLOADED' | 'CERTIFIED' | 'OFFLINE' | 'TRANSFERRED'
  rightType: 'RIGHT_OWNERSHIP' | 'RIGHT_USAGE'
  licenseType: 'PERSONAL' | 'COMMERCIAL' | 'EXCLUSIVE'
  chainTxHash?: string
  blockNumber?: number
  fileUrl?: string
  createTime: string
}

export interface UploadWorkRequest {
  fileName: string
  summary?: string
  modelName?: string
  modelVersion?: string
  modelSource?: string
  modelParams?: Record<string, any>
  prompt?: string
  creationType?: string
}

export interface CopyrightTransfer {
  id: number
  transferId: string
  workId: string
  fromUserId: number
  fromUserName?: string
  toUserId: number
  toUserName?: string
  fromAddress: string
  toAddress: string
  transferType: 'FULL_TRANSFER' | 'LICENSE_GRANT'
  currentRightType: string
  licenseType?: 'PERSONAL' | 'COMMERCIAL' | 'EXCLUSIVE'
  effectiveTime?: string
  expireTime?: string
  chainTxHash?: string
  blockNumber?: number
  blockTime?: string
  chainStatus: 'PENDING' | 'SUCCESS' | 'FAILED'
  transferStatus: 'INIT' | 'CONFIRMED' | 'CANCELLED' | 'EXPIRED'
  tradeDesc?: string
  price?: number
  createTime: string
}

export interface TransferRequest {
  workId: string
  toUserId: number
  transferType: 'FULL_TRANSFER' | 'LICENSE_GRANT'
  licenseType?: 'PERSONAL' | 'COMMERCIAL' | 'EXCLUSIVE'
  effectiveTime?: string
  expireTime?: string
  tradeDesc?: string
  price?: number
}