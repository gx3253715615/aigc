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
      <header class="header"><h1>Blockchain Query</h1></header>

      <div class="content-area">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card header="Query Certificate">
              <el-form @submit.prevent="queryCertificate">
                <el-form-item label="Work ID">
                  <el-input v-model="certWorkId" placeholder="Enter work ID" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="queryCertificate" :loading="certLoading">Query</el-button>
                </el-form-item>
              </el-form>
              <div v-if="certInfo" class="result-box">
                <el-descriptions title="Certificate Info" :column="1" border>
                  <el-descriptions-item label="Work ID">{{ certInfo.workId }}</el-descriptions-item>
                  <el-descriptions-item label="Exists">
                    <el-tag :type="certInfo.exists ? 'success' : 'danger'">
                      {{ certInfo.exists ? 'Yes' : 'No' }}
                    </el-tag>
                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </el-card>
          </el-col>

          <el-col :span="12">
            <el-card header="Verify Copyright">
              <el-form @submit.prevent="verifyCopyright">
                <el-form-item label="Work ID">
                  <el-input v-model="verifyWorkId" placeholder="Enter work ID" />
                </el-form-item>
                <el-form-item label="File Hash">
                  <el-input v-model="verifyHash" placeholder="Enter file hash" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="verifyCopyright" :loading="verifyLoading">Verify</el-button>
                </el-form-item>
              </el-form>
              <div v-if="verifyResult !== null" class="result-box">
                <el-result
                  :icon="verifyResult ? 'success' : 'error'"
                  :title="verifyResult ? 'Verified' : 'Not Verified'"
                  :sub-title="verifyResult ? 'Copyright is valid on blockchain' : 'Copyright not found or invalid'"
                />
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getCertificate, verifyCopyright as verifyApi } from '@/api/blockchain'
import type { CertificateInfo } from '@/api/blockchain'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const certWorkId = ref('')
const certLoading = ref(false)
const certInfo = ref<CertificateInfo | null>(null)

const verifyWorkId = ref('')
const verifyHash = ref('')
const verifyLoading = ref(false)
const verifyResult = ref<boolean | null>(null)

const handleMenuSelect = (index: string) => router.push(index)
const handleLogout = () => {
  userStore.logout()
  router.push('/login')
  ElMessage.success('Logout successful')
}

const queryCertificate = async () => {
  if (!certWorkId.value) {
    ElMessage.warning('Please enter work ID')
    return
  }
  certLoading.value = true
  try {
    const response = await getCertificate(certWorkId.value)
    certInfo.value = response.data
  } catch (error: any) {
    ElMessage.error('Query failed: ' + error.message)
  } finally {
    certLoading.value = false
  }
}

const verifyCopyright = async () => {
  if (!verifyWorkId.value || !verifyHash.value) {
    ElMessage.warning('Please enter all fields')
    return
  }
  verifyLoading.value = true
  try {
    const response = await verifyApi(verifyWorkId.value, verifyHash.value)
    verifyResult.value = response.data
  } catch (error: any) {
    ElMessage.error('Verification failed: ' + error.message)
  } finally {
    verifyLoading.value = false
  }
}
</script>

<style scoped>
@import './DashboardPage.vue';
.content-area {
  padding: 32px;
}
.result-box {
  margin-top: 24px;
}
</style>
