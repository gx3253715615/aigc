<template>
  <DefaultLayout>
    <template #header-left>
      <div class="page-title">
        <div class="title">我的作品</div>
        <div class="subtitle">上传、确权与转让你的 AIGC 作品</div>
      </div>
    </template>
    <template #header-actions>
      <el-button type="primary" @click="goToUpload">
        <el-icon><Plus /></el-icon>
        上传新作品
      </el-button>
    </template>

    <el-card class="panel" shadow="never">
      <el-table :data="works" v-loading="loading" style="width: 100%">
        <el-table-column prop="fileName" label="文件名" min-width="220" />
        <el-table-column prop="fileType" label="类型" width="110" />
        <el-table-column label="状态" width="130">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.workStatus)">{{ row.workStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="上传时间" width="180" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="viewWork(row)">查看</el-button>
            <el-button
              v-if="row.workStatus === 'UPLOADED'"
              size="small"
              type="success"
              @click="certifyWork(row)"
            >
              确权
            </el-button>
            <el-button
              v-if="row.workStatus === 'CERTIFIED'"
              size="small"
              type="warning"
              @click="transferWork(row)"
            >
              转让
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

    <!-- Work Detail Dialog -->
    <el-dialog v-model="detailVisible" title="作品详情" width="700px">
      <div v-if="selectedWork" class="work-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="作品 ID">{{ selectedWork.workId }}</el-descriptions-item>
          <el-descriptions-item label="文件名">{{ selectedWork.fileName }}</el-descriptions-item>
          <el-descriptions-item label="文件类型">{{ selectedWork.fileType }}</el-descriptions-item>
          <el-descriptions-item label="文件大小">{{ formatSize(selectedWork.fileSize) }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(selectedWork.workStatus)">{{ selectedWork.workStatus }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="权利类型">{{ selectedWork.rightType }}</el-descriptions-item>
          <el-descriptions-item label="上传时间" :span="2">{{ selectedWork.createTime }}</el-descriptions-item>
          <el-descriptions-item label="文件哈希" :span="2">{{ selectedWork.fileHash }}</el-descriptions-item>
          <el-descriptions-item v-if="selectedWork.chainTxHash" label="交易哈希" :span="2">
            {{ selectedWork.chainTxHash }}
          </el-descriptions-item>
          <el-descriptions-item v-if="selectedWork.summary" label="摘要" :span="2">
            {{ selectedWork.summary }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- Transfer Dialog -->
    <el-dialog v-model="transferVisible" title="版权转让" width="600px">
      <el-form :model="transferForm" label-width="140px">
        <el-form-item label="用户 ID">
          <el-input v-model.number="transferForm.toUserId" placeholder="请输入用户 ID" />
        </el-form-item>
        <el-form-item label="转让类型">
          <el-radio-group v-model="transferForm.transferType">
            <el-radio label="FULL_TRANSFER">全部转让</el-radio>
            <el-radio label="LICENSE_GRANT">许可授权</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="transferForm.transferType === 'LICENSE_GRANT'" label="许可类型">
          <el-select v-model="transferForm.licenseType" placeholder="请选择许可类型">
            <el-option label="个人使用" value="PERSONAL" />
            <el-option label="商业使用" value="COMMERCIAL" />
            <el-option label="独占许可" value="EXCLUSIVE" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述说明">
          <el-input v-model="transferForm.tradeDesc" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="transferVisible = false">取消</el-button>
        <el-button type="primary" @click="handleTransfer" :loading="transfering">确定</el-button>
      </template>
    </el-dialog>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { getMyWorks, certifyWork as certifyWorkApi } from '@/api/works'
import { createTransfer } from '@/api/transfers'
import type { Work } from '@/types/work'
import { Plus } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

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
    ElMessage.error('加载作品列表失败')
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
    ElMessage.success('版权确权成功!')
    loadWorks() // Refresh the works list
  } catch (error: any) {
    ElMessage.error('确权失败：' + error.message)
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
    ElMessage.success('转让创建成功!')
    transferVisible.value = false
    loadWorks() // Refresh the works list
  } catch (error: any) {
    ElMessage.error('转让失败：' + error.message)
  } finally {
    transfering.value = false
  }
}

onMounted(() => {
  loadWorks()
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

.panel {
  border: 1px solid rgba(2, 6, 23, 0.08);
  background: rgba(255, 255, 255, 0.72);
  backdrop-filter: blur(10px);
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
