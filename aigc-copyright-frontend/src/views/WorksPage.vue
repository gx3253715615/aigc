<template>
  <div class="dashboard-layout">
    <aside class="sidebar">
      <div class="sidebar-header">
        <h2>AIGC Copyright</h2>
      </div>
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
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getMyWorks, certifyWork as certifyWorkApi } from '@/api/works'
import { createTransfer } from '@/api/transfers'
import type { Work } from '@/types/work'

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

const handleMenuSelect = (index: string) => router.push(index)
const handleLogout = () => {
  userStore.logout()
  router.push('/login')
  ElMessage.success('Logout successful')
}

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
  selectedWork.value = work
  detailVisible.value = true
}

const certifyWork = async (work: Work) => {
  try {
    await certifyWorkApi(work.id)
    ElMessage.success('Copyright certification successful!')
    loadWorks()
  } catch (error: any) {
    ElMessage.error('Certification failed: ' + error.message)
  }
}

const transferWork = (work: Work) => {
  transferForm.value.workId = work.workId
  transferVisible.value = true
}

const handleTransfer = async () => {
  if (!transferForm.value.toUserId) {
    ElMessage.warning('Please enter user ID')
    return
  }
  transfering.value = true
  try {
    await createTransfer(transferForm.value)
    ElMessage.success('Transfer successful!')
    transferVisible.value = false
    loadWorks()
  } catch (error: any) {
    ElMessage.error('Transfer failed: ' + error.message)
  } finally {
    transfering.value = false
  }
}

onMounted(() => loadWorks())
</script>

<style scoped>
@import './DashboardPage.vue';
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
