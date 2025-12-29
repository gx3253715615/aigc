<template>
  <DefaultLayout>
    <header class="header"><h1>Transfer History</h1></header>

    <div class="content-area">
      <el-card>
        <el-form inline>
          <el-form-item label="Work ID">
            <el-input v-model="searchWorkId" placeholder="Enter work ID" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadTransfers">Search</el-button>
          </el-form-item>
        </el-form>

        <el-table :data="transfers" v-loading="loading" style="width: 100%; margin-top: 20px;">
          <el-table-column prop="transferId" label="Transfer ID" min-width="150" />
          <el-table-column prop="fromUserName" label="From User" width="120" />
          <el-table-column prop="toUserName" label="To User" width="120" />
          <el-table-column prop="transferType" label="Type" width="140" />
          <el-table-column label="Status" width="120">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.chainStatus)">{{ row.chainStatus }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="Transfer Time" width="180" />
        </el-table>

        <div class="pagination">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="total"
            layout="total, prev, pager, next"
            @current-change="loadTransfers"
          />
        </div>
      </el-card>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { getTransferHistory } from '@/api/transfers'
import type { CopyrightTransfer } from '@/types/work'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const loading = ref(false)
const searchWorkId = ref('')
const transfers = ref<CopyrightTransfer[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const getStatusType = (status: string) => {
  return status === 'SUCCESS' ? 'success' : status === 'PENDING' ? 'warning' : 'danger'
}

const loadTransfers = async () => {
  if (!searchWorkId.value) {
    ElMessage.warning('Please enter a work ID')
    return
  }
  loading.value = true
  try {
    const response = await getTransferHistory(searchWorkId.value, currentPage.value, pageSize.value)
    transfers.value = response.data.records
    total.value = response.data.totalRow
  } catch (error: any) {
    ElMessage.error('Failed to load transfers')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.dashboard-layout {
  display: flex;
  min-height: 100vh;
  background: #f5f7fa;
}

.sidebar {
  width: 240px;
  background: white;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 24px 20px;
  border-bottom: 1px solid #e4e7ed;
}

.sidebar-header h2 {
  font-size: 20px;
  font-weight: 700;
  color: #2c3e50;
}

.sidebar-menu {
  flex: 1;
  border: none;
}

.sidebar-footer {
  padding: 20px;
  border-top: 1px solid #e4e7ed;
}

.sidebar-footer .el-button {
  width: 100%;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header {
  background: white;
  padding: 0 32px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.header h1 {
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
}

.user-avatar {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 8px;
  transition: background 0.3s;
}

.user-avatar:hover {
  background: #f5f7fa;
}

.dashboard-content {
  flex: 1;
  padding: 32px;
  overflow-y: auto;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-info h3 {
  font-size: 28px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 4px 0;
}

.stat-info p {
  font-size: 14px;
  color: #7f8c8d;
  margin: 0;
}

.section-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  margin-top: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #2c3e50;
}

.content-area {
  padding: 32px;
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
