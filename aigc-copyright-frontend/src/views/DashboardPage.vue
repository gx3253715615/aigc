<template>
  <div class="dashboard-layout">
    <!-- Sidebar -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <h2>AIGC Copyright</h2>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        @select="handleMenuSelect"
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>Dashboard</span>
        </el-menu-item>
        <el-menu-item index="/works">
          <el-icon><Document /></el-icon>
          <span>My Works</span>
        </el-menu-item>
        <el-menu-item index="/upload">
          <el-icon><Upload /></el-icon>
          <span>Upload Work</span>
        </el-menu-item>
        <el-menu-item index="/transfers">
          <el-icon><Swap /></el-icon>
          <span>Transfers</span>
        </el-menu-item>
        <el-menu-item index="/blockchain">
          <el-icon><Link /></el-icon>
          <span>Blockchain</span>
        </el-menu-item>
      </el-menu>

      <div class="sidebar-footer">
        <el-button type="danger" plain @click="handleLogout">
          <el-icon><SwitchButton /></el-icon>
          Logout
        </el-button>
      </div>
    </aside>

    <!-- Main Content -->
    <div class="main-content">
      <!-- Header -->
      <header class="header">
        <div class="header-left">
          <h1>Dashboard</h1>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <div class="user-avatar">
              <el-icon><User /></el-icon>
              <span>{{ userStore.user?.username || 'User' }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">Profile</el-dropdown-item>
                <el-dropdown-item command="wallet">Wallet</el-dropdown-item>
                <el-dropdown-item divided command="logout">Logout</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <!-- Dashboard Content -->
      <div class="dashboard-content">
        <!-- Stats Cards -->
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="6">
            <div class="stat-card">
              <div class="stat-icon" style="background: #ecf5ff; color: #409eff;">
                <el-icon size="32"><Document /></el-icon>
              </div>
              <div class="stat-info">
                <h3>{{ stats.totalWorks }}</h3>
                <p>Total Works</p>
              </div>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <div class="stat-card">
              <div class="stat-icon" style="background: #f0f9ff; color: #67c23a;">
                <el-icon size="32"><CircleCheck /></el-icon>
              </div>
              <div class="stat-info">
                <h3>{{ stats.certifiedWorks }}</h3>
                <p>Certified Works</p>
              </div>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <div class="stat-card">
              <div class="stat-icon" style="background: #fef0f0; color: #f56c6c;">
                <el-icon size="32"><Swap /></el-icon>
              </div>
              <div class="stat-info">
                <h3>{{ stats.totalTransfers }}</h3>
                <p>Transfers</p>
              </div>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <div class="stat-card">
              <div class="stat-icon" style="background: #f4f4f5; color: #909399;">
                <el-icon size="32"><Wallet /></el-icon>
              </div>
              <div class="stat-info">
                <h3 style="font-size: 14px;">{{ shortAddress }}</h3>
                <p>Wallet</p>
              </div>
            </div>
          </el-col>
        </el-row>

        <!-- Recent Works -->
        <div class="section-card">
          <div class="section-header">
            <h2>Recent Works</h2>
            <el-button type="primary" @click="goToWorks">View All</el-button>
          </div>
          
          <el-table :data="recentWorks" v-loading="loading" style="width: 100%">
            <el-table-column prop="fileName" label="File Name" />
            <el-table-column prop="fileType" label="Type" width="100" />
            <el-table-column prop="workStatus" label="Status" width="120">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.workStatus)">
                  {{ row.workStatus }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="Upload Time" width="180" />
            <el-table-column label="Actions" width="150">
              <template #default="{ row }">
                <el-button size="small" @click="viewWork(row.id)">View</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getMyWorks } from '@/api/works'
import type { Work } from '@/types/work'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
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

const handleMenuSelect = (index: string) => {
  router.push(index)
}

const handleCommand = (command: string) => {
  if (command === 'logout') {
    handleLogout()
  } else if (command === 'profile') {
    ElMessage.info('Profile page coming soon')
  } else if (command === 'wallet') {
    ElMessage.info('Wallet page coming soon')
  }
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
  ElMessage.success('Logout successful')
}

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
    const response = await getMyWorks(1, 5)
    recentWorks.value = response.data.records
    stats.value.totalWorks = response.data.totalRow
    stats.value.certifiedWorks = response.data.records.filter(
      (work: Work) => work.workStatus === 'CERTIFIED'
    ).length
  } catch (error: any) {
    ElMessage.error('Failed to load dashboard data')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadDashboardData()
})
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
</style>
