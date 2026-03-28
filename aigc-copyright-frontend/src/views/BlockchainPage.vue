<template>
  <DefaultLayout>
    <template #header-left>
      <div class="page-title">
        <div class="title">区块链查询</div>
        <div class="subtitle">通过作品 ID 查询链上存证详情</div>
      </div>
    </template>

    <div class="query-container">
      <el-card class="panel query-card" shadow="never">
        <template #header>
          <div class="card-header">
            <span>链上作品信息查询</span>
          </div>
        </template>
        
        <el-form :inline="true" class="query-form" @submit.prevent="handleQuery">
          <el-form-item label="作品主键 ID">
            <el-input 
              v-model="workId" 
              placeholder="请输入数据库作品主键 ID" 
              clearable 
              class="query-input"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery" :loading="loading">
              <el-icon class="el-icon--left"><Search /></el-icon>
              查询链上信息
            </el-button>
          </el-form-item>
        </el-form>

        <div class="tips">
          <el-alert
            title="查询说明"
            type="info"
            description="请输入作品在数据库中的主键 ID（Long 类型），系统将自动从区块链节点获取该作品的最新存证状态、哈希值、作者地址及确权时间。"
            show-icon
            :closable="false"
          />
        </div>
      </el-card>
    </div>

    <!-- 链上信息展示对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="区块链存证详情"
      width="600px"
      destroy-on-close
      class="blockchain-dialog"
    >
      <div v-if="blockchainInfo" class="blockchain-info">
        <div class="info-status">
          <el-result
            icon="success"
            title="已在链上存证"
            sub-title="该作品信息已通过智能合约验证"
          >
            <template #extra>
              <div class="cert-time">确权时间：{{ formatTime(blockchainInfo.certifyTime) }}</div>
            </template>
          </el-result>
        </div>

        <el-descriptions :column="1" border class="info-details">
          <el-descriptions-item label="作品 ID (WorkId)">
            <span class="mono">{{ blockchainInfo.workId }}</span>
            <el-button link type="primary" size="small" class="copy-btn" @click="copyText(blockchainInfo.workId)">复制</el-button>
          </el-descriptions-item>
          
          <el-descriptions-item label="文件哈希 (FileHash)">
            <div class="hash-content">
              <span class="mono break-all">{{ blockchainInfo.fileHash }}</span>
              <el-button link type="primary" size="small" @click="copyText(blockchainInfo.fileHash)">复制</el-button>
            </div>
          </el-descriptions-item>
          
          <el-descriptions-item label="作者地址 (Author)">
            <div class="hash-content">
              <span class="mono break-all">{{ blockchainInfo.author }}</span>
              <el-button link type="primary" size="small" @click="copyText(blockchainInfo.author)">复制</el-button>
            </div>
          </el-descriptions-item>
          
          <el-descriptions-item label="确权时间 (CertifyTime)">
            <span>{{ formatTime(blockchainInfo.certifyTime) }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button type="primary" @click="dialogVisible = false">确认</el-button>
      </template>
    </el-dialog>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { getWorkFromBlockchain } from '@/api/blockchain'
import type { BlockchainWorkInfo } from '@/api/blockchain'

const workId = ref('')
const loading = ref(false)
const dialogVisible = ref(false)
const blockchainInfo = ref<BlockchainWorkInfo | null>(null)

const handleQuery = async () => {
  if (!workId.value) {
    ElMessage.warning('请输入作品主键 ID')
    return
  }

  loading.value = true
  try {
    const response = await getWorkFromBlockchain(workId.value)
    blockchainInfo.value = response.data
    dialogVisible.value = true
  } catch (error: any) {
    ElMessage.error('查询失败：' + (error.message || '该作品可能尚未确权上链'))
  } finally {
    loading.value = false
  }
}

const formatTime = (time: string | number) => {
  if (!time || time === '0' || time === 0) return '尚未确权'
  return String(time)
}

const copyText = (text: string) => {
  navigator.clipboard.writeText(text).then(() => {
    ElMessage.success('已复制到剪贴板')
  })
}
</script>

<style scoped>
.page-title {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.page-title .title {
  font-size: 18px;
  font-weight: 750;
  color: rgba(15, 23, 42, 0.92);
}

.page-title .subtitle {
  font-size: 12px;
  color: rgba(15, 23, 42, 0.6);
}

.query-container {
  max-width: 800px;
  margin: 0 auto;
  padding-top: 40px;
}

.query-card {
  border: 1px solid rgba(2, 6, 23, 0.08);
  background: rgba(255, 255, 255, 0.72);
  backdrop-filter: blur(10px);
}

.card-header {
  font-weight: 600;
  font-size: 16px;
  color: #1e293b;
}

.query-form {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.query-input {
  width: 300px;
}

.tips {
  margin-top: 30px;
}

.blockchain-info {
  padding: 10px 0;
}

.info-status {
  margin-bottom: 20px;
}

.info-status :deep(.el-result) {
  padding: 0;
}

.cert-time {
  font-size: 14px;
  color: #64748b;
  font-weight: 500;
}

.info-details {
  margin-top: 20px;
}

.mono {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
  font-size: 13px;
  color: #334155;
}

.break-all {
  word-break: break-all;
}

.hash-content {
  display: flex;
  align-items: center;
  gap: 10px;
  justify-content: space-between;
}

.timestamp-label {
  font-size: 12px;
  color: #94a3b8;
  margin-left: 8px;
}

.copy-btn {
  margin-left: 8px;
}

:deep(.el-descriptions__label) {
  width: 160px;
  font-weight: 600;
  color: #475569;
}
</style>
