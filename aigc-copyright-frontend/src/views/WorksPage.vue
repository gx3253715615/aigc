<template>
  <DefaultLayout>
    <header class="header">
      <h1>My Works</h1>
      <el-button type="primary" @click="goToUpload">
        <el-icon><Plus /></el-icon>
        Upload New Work
      </el-button>
    </header>

    <div class="works-content">
      <el-card>
        <el-table :data="works" v-loading="loading" style="width: 100%">
          <el-table-column prop="fileName" label="File Name" min-width="200" />
          <el-table-column prop="fileType" label="Type" width="100" />
          <el-table-column label="Status" width="120">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.workStatus)">{{ row.workStatus }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="Upload Time" width="180" />
          <el-table-column label="Actions" width="250" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="viewWork(row)">View</el-button>
              <el-button 
                v-if="row.workStatus === 'UPLOADED'" 
                size="small" 
                type="success" 
                @click="certifyWork(row)"
              >
                Certify
              </el-button>
              <el-button 
                v-if="row.workStatus === 'CERTIFIED'" 
                size="small" 
                type="warning" 
                @click="transferWork(row)"
              >
                Transfer
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="loadWorks"
            @current-change="loadWorks"
          />
        </div>
      </el-card>
    </div>

    <!-- Work Detail Dialog -->
    <el-dialog v-model="detailVisible" title="Work Details" width="700px">
      <div v-if="selectedWork" class="work-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Work ID">{{ selectedWork.workId }}</el-descriptions-item>
          <el-descriptions-item label="File Name">{{ selectedWork.fileName }}</el-descriptions-item>
          <el-descriptions-item label="File Type">{{ selectedWork.fileType }}</el-descriptions-item>
          <el-descriptions-item label="File Size">{{ formatSize(selectedWork.fileSize) }}</el-descriptions-item>
          <el-descriptions-item label="Status">
            <el-tag :type="getStatusType(selectedWork.workStatus)">{{ selectedWork.workStatus }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Right Type">{{ selectedWork.rightType }}</el-descriptions-item>
          <el-descriptions-item label="Upload Time" :span="2">{{ selectedWork.createTime }}</el-descriptions-item>
          <el-descriptions-item label="File Hash" :span="2">{{ selectedWork.fileHash }}</el-descriptions-item>
          <el-descriptions-item v-if="selectedWork.chainTxHash" label="Transaction Hash" :span="2">
            {{ selectedWork.chainTxHash }}
          </el-descriptions-item>
          <el-descriptions-item v-if="selectedWork.summary" label="Summary" :span="2">
            {{ selectedWork.summary }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- Transfer Dialog -->
    <el-dialog v-model="transferVisible" title="Transfer Copyright" width="600px">
      <el-form :model="transferForm" label-width="140px">
        <el-form-item label="To User ID">
          <el-input v-model.number="transferForm.toUserId" placeholder="Enter user ID" />
        </el-form-item>
        <el-form-item label="Transfer Type">
          <el-radio-group v-model="transferForm.transferType">
            <el-radio label="FULL_TRANSFER">Full Transfer</el-radio>
            <el-radio label="LICENSE_GRANT">License Grant</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="transferForm.transferType === 'LICENSE_GRANT'" label="License Type">
          <el-select v-model="transferForm.licenseType" placeholder="Select license type">
            <el-option label="Personal" value="PERSONAL" />
            <el-option label="Commercial" value="COMMERCIAL" />
            <el-option label="Exclusive" value="EXCLUSIVE" />
          </el-select>
        </el-form-item>
        <el-form-item label="Description">
          <el-input v-model="transferForm.tradeDesc" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="transferVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleTransfer" :loading="transfering">Confirm</el-button>
      </template>
    </el-dialog>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { getMyWorks, certifyWork as certifyWorkApi } from '@/api/works'
import { createTransfer } from '@/api/transfers'
import type { Work } from '@/types/work'
import { Plus } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const loading = ref(false)
const works = ref<Work[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const selectedWork = ref<Work | null>(null)
const transferVisible = ref(false)
const transfering = ref(false)

const transferForm = ref({
  workId: '',
  toUserId: 0,
  transferType: 'FULL_TRANSFER' as 'FULL_TRANSFER' | 'LICENSE_GRANT',
  licenseType: undefined as 'PERSONAL' | 'COMMERCIAL' | 'EXCLUSIVE' | undefined,
  tradeDesc: ''
})

const goToUpload = () => router.push('/upload')

const getStatusType = (status: string) => {
  const types: Record<string, any> = {
    'UPLOADED': 'info',
    'CERTIFIED': 'success',
    'TRANSFERRED': 'warning',
    'OFFLINE': 'danger'
  }
  return types[status] || 'info'
}

const formatSize = (size: number) => {
  if (size < 1024) return size + ' B'
  if (size < 1024 * 1024) return (size / 1024).toFixed(2) + ' KB'
  return (size / 1024 / 1024).toFixed(2) + ' MB'
}

const loadWorks = async () => {
  loading.value = true
  try {
    const response = await getMyWorks(currentPage.value, pageSize.value)
    works.value = response.data.records
    total.value = response.data.totalRow
  } catch (error: any) {
    ElMessage.error('Failed to load works')
  } finally {
    loading.value = false
  }
}

const viewWork = (work: Work) => {
  router.push(`/works/${work.id}`)
}

const certifyWork = async (work: Work) => {
  if (userStore.user?.authStatus !== 'AUTH') {
    ElMessage.warning('您尚未通过实名认证，无法确权作品')
    return
  }
  
  try {
    await certifyWorkApi(work.id)
    ElMessage.success('Copyright certification successful!')
    loadWorks() // Refresh the works list
  } catch (error: any) {
    ElMessage.error('Certification failed: ' + error.message)
  }
}

const transferWork = (work: Work) => {
  if (userStore.user?.authStatus !== 'AUTH') {
    ElMessage.warning('您尚未通过实名认证，无法转让版权')
    return
  }
  
  transferForm.value.workId = work.id.toString()
  transferVisible.value = true
}

const handleTransfer = async () => {
  transfering.value = true
  try {
    await createTransfer({
      workId: transferForm.value.workId,
      toUserId: transferForm.value.toUserId,
      transferType: transferForm.value.transferType,
      licenseType: transferForm.value.licenseType,
      tradeDesc: transferForm.value.tradeDesc
    })
    ElMessage.success('Transfer created successfully!')
    transferVisible.value = false
    loadWorks() // Refresh the works list
  } catch (error: any) {
    ElMessage.error('Transfer failed: ' + error.message)
  } finally {
    transfering.value = false
  }
}

onMounted(() => {
  loadWorks()
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

.works-content {
  padding: 32px;
}
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
.work-detail {
  padding: 20px 0;
}
</style>
