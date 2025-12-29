<template>
  <DefaultLayout>
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
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
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
.result-box {
  margin-top: 24px;
}
</style>
