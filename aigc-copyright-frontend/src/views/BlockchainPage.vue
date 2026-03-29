<template>
  <DefaultLayout>
    <template #header-left>
      <div class="page-title">
        <div class="title">区块链查询</div>
        <div class="subtitle">实时调取链上存证、流转及交易详情</div>
      </div>
    </template>

    <div class="query-container">
      <!-- 第一行：作品存证与单笔交易查询 -->
      <el-row :gutter="32" class="query-row">
        <el-col :span="12">
          <el-card class="panel query-card" shadow="never">
            <template #header>
              <div class="card-header">
                <el-icon><Document /></el-icon>
                <span>作品链上存证查询</span>
              </div>
            </template>
            <div class="card-body">
              <p class="desc">输入作品 WorkID 获取链上原始确权存证信息</p>
              <el-form class="query-form" @submit.prevent="handleWorkQuery">
                <el-form-item>
                  <el-input v-model="workId" placeholder="请输入作品 WorkID" clearable />
                </el-form-item>
                <el-button type="primary" class="w-full" @click="handleWorkQuery" :loading="workLoading">
                  <el-icon class="el-icon--left"><Search /></el-icon>
                  查询存证信息
                </el-button>
              </el-form>
            </div>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card class="panel query-card" shadow="never">
            <template #header>
              <div class="card-header">
                <el-icon><Coin /></el-icon>
                <span>单笔交易详细查询</span>
              </div>
            </template>
            <div class="card-body">
              <p class="desc">输入单笔交易 ID 获取其链上完整原始数据</p>
              <el-form class="query-form" @submit.prevent="handleSingleTransferQuery">
                <el-form-item>
                  <el-input v-model="singleTransferId" placeholder="请输入交易 ID (TransferID)" clearable />
                </el-form-item>
                <el-button type="warning" class="w-full" @click="handleSingleTransferQuery" :loading="singleLoading">
                  <el-icon class="el-icon--left"><Search /></el-icon>
                  查询交易详情
                </el-button>
              </el-form>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 第二行：作品完整流转历史 -->
      <el-row class="query-row">
        <el-col :span="24">
          <el-card class="panel query-card" shadow="never">
            <template #header>
              <div class="card-header">
                <el-icon><Refresh /></el-icon>
                <span>作品交易流转全记录</span>
              </div>
            </template>
            <div class="card-body">
              <p class="desc">通过 WorkID 深度穿透查询该作品在链上的所有流转历史，并按时间排序展示</p>
              <el-form :inline="true" class="query-form inline-form" @submit.prevent="handleFullHistoryQuery">
                <el-form-item label="作品 WorkID">
                  <el-input v-model="historyWorkId" placeholder="请输入作品 WorkID" clearable class="history-input" />
                </el-form-item>
                <el-form-item>
                  <el-button type="success" @click="handleFullHistoryQuery" :loading="historyLoading">
                    <el-icon class="el-icon--left"><List /></el-icon>
                    获取完整流转历史
                  </el-button>
                </el-form-item>
              </el-form>

              <div v-if="historyRecords.length > 0" class="history-result">
                <el-divider content-position="left">流转时间轴</el-divider>
                <el-timeline>
                  <el-timeline-item
                    v-for="record in historyRecords"
                    :key="record.transferId"
                    :timestamp="record.timestamp"
                    placement="top"
                    :type="record.transferType === 'FULL_TRANSFER' || record.transferType === '0' ? 'warning' : 'success'"
                  >
                    <el-card shadow="hover" class="record-card">
                      <div class="record-header">
                        <el-tag :type="record.transferType === 'FULL_TRANSFER' || record.transferType === '0' ? 'warning' : 'success'" size="small">
                          {{ (record.transferType === 'FULL_TRANSFER' || record.transferType === '0') ? '所有权转让' : '使用权授权' }}
                        </el-tag>
                        <span class="record-id">ID: {{ record.transferId }}</span>
                      </div>
                      <div class="record-detail">
                        <div class="address-row">
                          <span class="label">From:</span> <span class="mono">{{ record.from }}</span>
                        </div>
                        <div class="address-row">
                          <span class="label">To:</span> <span class="mono">{{ record.to }}</span>
                        </div>
                      </div>
                    </el-card>
                  </el-timeline-item>
                </el-timeline>
              </div>
              <el-empty v-else-if="hasQueriedHistory" description="该作品暂无链上流转记录" />
            </div>
          </el-card>
        </el-col>
      </el-row>

      <div class="tips-container">
        <el-alert
          title="技术说明"
          type="info"
          description="本查询直接通过区块链节点 RPC 接口访问分布式账本。作品存证信息包含数字摘要、作者身份及确权时间戳；交易流转记录通过追溯智能合约中的 Transfer 列表实现，确保所有版权变更轨迹真实不可篡改。"
          show-icon
          :closable="false"
        />
      </div>
    </div>

    <!-- 作品存证详情弹窗 -->
    <el-dialog v-model="workDialogVisible" title="区块链存证详情" width="600px" destroy-on-close>
      <div v-if="blockchainInfo" class="dialog-content">
        <div class="status-center">
          <el-result icon="success" title="存证验证通过" sub-title="区块链数据一致性校验成功" />
        </div>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="作品 ID (WorkId)">
            <span class="mono">{{ blockchainInfo.workId }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="文件哈希 (FileHash)">
            <div class="flex-between">
              <span class="mono break-all">{{ blockchainInfo.fileHash }}</span>
              <el-button link type="primary" @click="copyText(blockchainInfo.fileHash)">复制</el-button>
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="作者地址 (Author)">
            <div class="flex-between">
              <span class="mono break-all">{{ blockchainInfo.author }}</span>
              <el-button link type="primary" @click="copyText(blockchainInfo.author)">复制</el-button>
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="当前持有者 (Owner)">
            <div class="flex-between">
              <span class="mono break-all text-warning bold">{{ blockchainInfo.owner }}</span>
              <el-button link type="warning" @click="copyText(blockchainInfo.owner)">复制</el-button>
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="确权时间">
            <span class="bold">{{ blockchainInfo.certifyTime }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- 交易详情弹窗 -->
    <el-dialog v-model="detailDialogVisible" title="链上交易详细数据" width="650px" destroy-on-close>
      <div v-if="transferDetail" class="dialog-content">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="交易 ID">
            <span class="mono">{{ transferDetail.transferId || singleTransferId }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="作品 ID">
            <span class="mono">{{ transferDetail.workId }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="交易类型">
            <el-tag :type="transferDetail.transferType === 'FULL_TRANSFER' || transferDetail.transferType === '0' ? 'warning' : 'success'">
              {{ (transferDetail.transferType === 'FULL_TRANSFER' || transferDetail.transferType === '0') ? '所有权转让' : '使用权授权' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="授权模式" v-if="transferDetail.transferType !== 'FULL_TRANSFER' && transferDetail.transferType !== '0'">
            <el-tag type="info">
              {{ 
                transferDetail.licenseType === 'PERSONAL' ? '个人授权' : 
                transferDetail.licenseType === 'COMMERCIAL' ? '商用授权' : 
                transferDetail.licenseType === 'EXCLUSIVE' ? '独占授权' : '无'
              }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="交易价格" v-if="transferDetail.price">
            <span class="bold text-primary">{{ transferDetail.price }} 元</span>
          </el-descriptions-item>
          <el-descriptions-item label="转让方 (From)">
            <span class="mono break-all">{{ transferDetail.from }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="受让方 (To)">
            <span class="mono break-all">{{ transferDetail.to }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="生效时间" v-if="transferDetail.effectiveTime">
            <span>{{ transferDetail.effectiveTime }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="过期时间" v-if="transferDetail.expireTime">
            <span>{{ transferDetail.expireTime }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="上链时间">
            <span class="bold">{{ transferDetail.transferTime || transferDetail.timestamp }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="文件哈希">
            <span class="mono break-all">{{ transferDetail.fileHash }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Document, Refresh, List, Coin } from '@element-plus/icons-vue'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { getWorkFromBlockchain, getTransferHistoryByWorkId, getBlockchainTransferDetail } from '@/api/blockchain'
import type { BlockchainWorkInfo } from '@/api/blockchain'

// 作品存证查询
const workId = ref('')
const workLoading = ref(false)
const workDialogVisible = ref(false)
const blockchainInfo = ref<BlockchainWorkInfo | null>(null)

// 单笔交易查询
const singleTransferId = ref('')
const singleLoading = ref(false)
const detailDialogVisible = ref(false)
const transferDetail = ref<any>(null)

// 完整历史查询
const historyWorkId = ref('')
const historyLoading = ref(false)
const historyRecords = ref<any[]>([])
const hasQueriedHistory = ref(false)

// 处理作品存证查询
const handleWorkQuery = async () => {
  if (!workId.value) return ElMessage.warning('请输入作品 WorkID')
  workLoading.value = true
  try {
    const res = await getWorkFromBlockchain(workId.value)
    blockchainInfo.value = res.data
    workDialogVisible.value = true
  } catch (error: any) {
    ElMessage.error(error.message || '查询存证失败，请确认 WorkID 是否正确且已上链')
  } finally {
    workLoading.value = false
  }
}

// 处理单笔交易查询
const handleSingleTransferQuery = async () => {
  if (!singleTransferId.value) return ElMessage.warning('请输入交易 ID')
  singleLoading.value = true
  try {
    const res = await getBlockchainTransferDetail(singleTransferId.value)
    transferDetail.value = {
      ...res.data,
      transferId: res.data.transferId || singleTransferId.value
    }
    detailDialogVisible.value = true
  } catch (error: any) {
    ElMessage.error(error.message || '查询交易详情失败')
  } finally {
    singleLoading.value = false
  }
}

// 处理完整流转历史查询 (批量获取详情并排序)
const handleFullHistoryQuery = async () => {
  if (!historyWorkId.value) return ElMessage.warning('请输入作品 WorkID')
  historyLoading.value = true
  historyRecords.value = []
  hasQueriedHistory.value = false
  try {
    // 1. 获取 ID 集合
    const idsRes = await getTransferHistoryByWorkId(historyWorkId.value)
    const ids = idsRes.data
    
    if (ids && ids.length > 0) {
      // 2. 并发查询详情
      const detailPromises = ids.map(id => getBlockchainTransferDetail(id))
      const results = await Promise.all(detailPromises)
      
      // 3. 提取数据、注入 ID 并排序
      const records = results.map((r, index) => ({
        ...r.data,
        transferId: r.data.transferId || ids[index],
        // 兼容 timestamp 和 transferTime
        timestamp: r.data.transferTime || r.data.timestamp
      }))
      
      records.sort((a, b) => {
        return (a.timestamp || '').localeCompare(b.timestamp || '')
      })
      historyRecords.value = records
    }
    hasQueriedHistory.value = true
  } catch (error: any) {
    ElMessage.error(error.message || '获取历史记录失败')
  } finally {
    historyLoading.value = false
  }
}

const copyText = (text: string) => {
  navigator.clipboard.writeText(text).then(() => ElMessage.success('已复制'))
}
</script>

<style scoped>
.page-title { display: flex; flex-direction: column; gap: 4px; }
.page-title .title { font-size: 20px; font-weight: 800; color: #0f172a; }
.page-title .subtitle { font-size: 13px; color: #64748b; }

.query-container { max-width: 1100px; margin: 32px auto 60px; padding: 0 20px; }
.query-row { margin-bottom: 32px; }
.query-card { 
  background: rgba(255, 255, 255, 0.85); 
  backdrop-filter: blur(12px); 
  border: 1px solid rgba(2, 6, 23, 0.08);
  height: 100%;
}
.card-header { display: flex; align-items: center; gap: 10px; font-weight: 700; font-size: 16px; color: #1e293b; }
.card-body { padding: 8px 4px; }
.card-body .desc { font-size: 13px; color: #64748b; margin-bottom: 20px; line-height: 1.5; }

.query-form { margin-top: 12px; }
.inline-form { display: flex; align-items: center; gap: 16px; }
.history-input { width: 320px; }
.w-full { width: 100%; }

.history-result { margin-top: 24px; padding: 16px; background: #f8fafc; border-radius: 12px; }
.record-card { border-radius: 8px; border: 1px solid #e2e8f0; }
.record-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.record-id { font-size: 12px; color: #94a3b8; font-family: ui-monospace, monospace; }
.record-detail { display: flex; flex-direction: column; gap: 6px; }
.address-row { display: flex; gap: 8px; font-size: 12px; }
.address-row .label { color: #64748b; font-weight: 600; width: 40px; }

.status-center { margin-bottom: 8px; }
.flex-between { display: flex; justify-content: space-between; align-items: center; gap: 12px; }
.mono { font-family: ui-monospace, monospace; font-size: 12px; color: #334155; }
.break-all { word-break: break-all; }
.bold { font-weight: 700; color: #0f172a; }
.text-warning { color: #e6a23c; }

.tips-container { margin-top: 48px; }

:deep(.el-descriptions__label) { width: 150px; font-weight: 600; background-color: #f8fafc; color: #475569; }
:deep(.el-card__header) { padding: 14px 20px; border-bottom: 1px solid #f1f5f9; }
</style>
