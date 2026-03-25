<template>
  <DefaultLayout>
    <template #header-left>
      <div class="page-title">
        <div class="title">工作台</div>
        <div class="subtitle">概览你的作品确权与流转情况</div>
      </div>
    </template>

    <div class="dashboard">
      <el-row :gutter="16">
        <el-col :xs="24" :sm="12" :md="6">
          <el-card class="stat-card" shadow="never">
            <div class="stat">
              <div class="stat-icon primary">
                <el-icon :size="20"><Document /></el-icon>
              </div>
              <div class="stat-main">
                <div class="stat-value">{{ stats.totalWorks }}</div>
                <div class="stat-label">作品总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <el-card class="stat-card" shadow="never">
            <div class="stat">
              <div class="stat-icon success">
                <el-icon :size="20"><CircleCheck /></el-icon>
              </div>
              <div class="stat-main">
                <div class="stat-value">{{ stats.certifiedWorks }}</div>
                <div class="stat-label">已确权作品</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <el-card class="stat-card" shadow="never">
            <div class="stat">
              <div class="stat-icon warning">
                <el-icon :size="20"><Refresh /></el-icon>
              </div>
              <div class="stat-main">
                <div class="stat-value">{{ stats.totalTransfers }}</div>
                <div class="stat-label">转让次数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <el-card class="stat-card" shadow="never">
            <div class="stat">
              <div class="stat-icon neutral">
                <el-icon :size="20"><Wallet /></el-icon>
              </div>
              <div class="stat-main">
                <div class="stat-value mono">{{ shortAddress }}</div>
                <div class="stat-label">钱包地址</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-card class="section-card" shadow="never">
        <template #header>
          <div class="section-header">
            <div class="section-title">
              <div class="h">最近作品</div>
              <div class="d">最近上传的 5 条记录</div>
            </div>
            <el-button type="primary" plain @click="goToWorks">查看全部</el-button>
          </div>
        </template>

        <el-table :data="recentWorks" v-loading="loading" style="width: 100%">
          <el-table-column prop="fileName" label="文件名" min-width="200" />
          <el-table-column prop="fileType" label="类型" width="110" />
          <el-table-column prop="workStatus" label="状态" width="130">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.workStatus)">{{ row.workStatus }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="上传时间" width="180" />
          <el-table-column label="操作" width="140" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="viewWork(row.id)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { getMyWorks } from '@/api/works'
import { getMyTransferHistory } from '@/api/transfers'
import type { Work } from '@/types/work'
import { Document, CircleCheck, Refresh, Wallet } from '@element-plus/icons-vue'

const router = useRouter()
const loading = ref(false)
const recentWorks = ref<Work[]>([])

const stats = ref({
  totalWorks: 0,
  certifiedWorks: 0,
  totalTransfers: 0
})

const shortAddress = computed(() => {
  const address = localStorage.getItem('walletAddress') || ''
  if (!address) return 'N/A'
  return `${address.slice(0, 6)}...${address.slice(-4)}`
})

const goToWorks = () => {
  router.push('/works')
}

const viewWork = (id: number) => {
  router.push(`/works/${id}`)
}

const getStatusType = (status: string) => {
  const types: Record<string, any> = {
    'UPLOADED': 'info',
    'CERTIFIED': 'success',
    'TRANSFERRED': 'warning',
    'OFFLINE': 'danger'
  }
  return types[status] || 'info'
}

const loadDashboardData = async () => {
  loading.value = true
  try {
    // Load recent works
    const response = await getMyWorks(1, 5) // Get first 5 works
    recentWorks.value = response.data.records
    
    // Calculate stats
    stats.value.totalWorks = response.data.totalRow
    stats.value.certifiedWorks = response.data.records.filter(work => work.workStatus === 'CERTIFIED').length
    const transferRes = await getMyTransferHistory('ALL', undefined, 1, 1)
    stats.value.totalTransfers = transferRes.data.totalRow
  } catch (error) {
    console.error('Failed to load dashboard data:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadDashboardData()
})
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

.dashboard {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.stat-card {
  border: 1px solid rgba(2, 6, 23, 0.08);
  background: rgba(255, 255, 255, 0.72);
  backdrop-filter: blur(10px);
}

.stat {
  display: flex;
  align-items: center;
  gap: 14px;
}

.stat-icon {
  width: 42px;
  height: 42px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid rgba(2, 6, 23, 0.06);
}

.stat-icon.primary {
  background: rgba(79, 70, 229, 0.12);
  color: rgba(79, 70, 229, 1);
}

.stat-icon.success {
  background: rgba(34, 197, 94, 0.12);
  color: rgba(34, 197, 94, 1);
}

.stat-icon.warning {
  background: rgba(245, 158, 11, 0.14);
  color: rgba(245, 158, 11, 1);
}

.stat-icon.neutral {
  background: rgba(15, 23, 42, 0.06);
  color: rgba(15, 23, 42, 0.8);
}

.stat-main {
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-width: 0;
}

.stat-value {
  font-size: 22px;
  font-weight: 780;
  color: rgba(15, 23, 42, 0.92);
}

.stat-value.mono {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
  font-size: 13px;
  font-weight: 700;
}

.stat-label {
  font-size: 12px;
  color: rgba(15, 23, 42, 0.6);
}

.section-card {
  border: 1px solid rgba(2, 6, 23, 0.08);
  background: rgba(255, 255, 255, 0.72);
  backdrop-filter: blur(10px);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.section-title {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.section-title .h {
  font-size: 14px;
  font-weight: 750;
  color: rgba(15, 23, 42, 0.9);
}

.section-title .d {
  font-size: 12px;
  color: rgba(15, 23, 42, 0.58);
}
</style>
