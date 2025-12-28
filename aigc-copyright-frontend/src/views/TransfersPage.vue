<template>
  <div class="dashboard-layout">
    <aside class="sidebar">
      <div class="sidebar-header"><h2>AIGC Copyright</h2></div>
      <el-menu :default-active="activeMenu" class="sidebar-menu" @select="handleMenuSelect">
        <el-menu-item index="/dashboard"><el-icon><HomeFilled /></el-icon><span>Dashboard</span></el-menu-item>
        <el-menu-item index="/works"><el-icon><Document /></el-icon><span>My Works</span></el-menu-item>
        <el-menu-item index="/upload"><el-icon><Upload /></el-icon><span>Upload Work</span></el-menu-item>
        <el-menu-item index="/transfers"><el-icon><Swap /></el-icon><span>Transfers</span></el-menu-item>
        <el-menu-item index="/blockchain"><el-icon><Link /></el-icon><span>Blockchain</span></el-menu-item>
      </el-menu>
      <div class="sidebar-footer">
        <el-button type="danger" plain @click="handleLogout"><el-icon><SwitchButton /></el-icon>Logout</el-button>
      </div>
    </aside>

    <div class="main-content">
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
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
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

const handleMenuSelect = (index: string) => router.push(index)
const handleLogout = () => {
  userStore.logout()
  router.push('/login')
  ElMessage.success('Logout successful')
}

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
@import './DashboardPage.vue';
.content-area {
  padding: 32px;
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
