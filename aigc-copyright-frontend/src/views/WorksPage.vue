<template>
  <DefaultLayout>
    <template #header-left>
      <div class="page-title">
        <div class="title">我的作品</div>
        <div class="subtitle">上传、确权与转让你的 AIGC 作品</div>
      </div>
    </template>

    <el-card class="panel" shadow="never">
      <el-table :data="works" v-loading="loading" style="width: 100%">
        <el-table-column prop="fileName" label="文件名" min-width="220" />
        <el-table-column prop="fileType" label="类型" width="110" />
        <el-table-column label="作品状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.workStatus)" size="small">
              {{ formatWorkStatus(row.workStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="审核状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getAuditStatusType(row.status)" size="small">
              {{ formatAuditStatus(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="上传时间" width="180" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="viewWork(row)">查看</el-button>
            <el-button
              v-if="row.workStatus === 'UPLOADED'"
              size="small"
              type="primary"
              plain
              :disabled="row.status !== 'PASS'"
              @click="certifyWork(row)"
            >
              确权
            </el-button>
            <el-button
              v-if="row.workStatus === 'CERTIFIED'"
              size="small"
              type="success"
              plain
              :disabled="row.status !== 'PASS'"
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
    <el-dialog
      v-model="detailVisible"
      title="作品详情"
      width="680px"
      top="7vh"
      class="work-detail-dialog"
    >
      <div class="work-detail" v-loading="detailLoading">
        <el-tabs v-if="selectedWork" v-model="detailTab" class="detail-tabs" stretch>
          <el-tab-pane label="基础" name="base">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="主键 ID">{{ selectedWork.id }}</el-descriptions-item>
              <el-descriptions-item label="作品 ID">{{ selectedWork.workId }}</el-descriptions-item>
              <el-descriptions-item label="用户 ID">{{ selectedWork.userId }}</el-descriptions-item>
              <el-descriptions-item label="用户名">{{ selectedWork.userName || '-' }}</el-descriptions-item>
              <el-descriptions-item label="文件名" :span="2">
                <div class="wrap">{{ selectedWork.fileName }}</div>
              </el-descriptions-item>
              <el-descriptions-item label="文件类型">{{ formatFileType(selectedWork.fileType) }}</el-descriptions-item>
              <el-descriptions-item label="文件大小">{{ formatSize(selectedWork.fileSize) }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusType(selectedWork.workStatus)">{{ formatWorkStatus(selectedWork.workStatus) }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="上传时间">{{ formatDateTime(selectedWork.createTime) }}</el-descriptions-item>
              <el-descriptions-item label="权利类型">
                <el-tag type="info">{{ formatRightType(selectedWork.rightType) }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="许可类型">
                <el-tag type="info">{{ formatLicenseType(selectedWork.licenseType) }}</el-tag>
              </el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>

          <el-tab-pane label="链上" name="chain">
            <el-descriptions :column="1" border>
              <el-descriptions-item label="文件哈希">
                <div class="mono wrap">{{ selectedWork.fileHash }}</div>
              </el-descriptions-item>
              <el-descriptions-item label="交易哈希">
                <div class="mono wrap">{{ selectedWork.chainTxHash || '-' }}</div>
              </el-descriptions-item>
              <el-descriptions-item label="区块高度">
                {{ selectedWork.blockNumber !== undefined && selectedWork.blockNumber !== null ? selectedWork.blockNumber : '-' }}
              </el-descriptions-item>
              <el-descriptions-item label="文件访问链接">
                <el-link v-if="selectedWork.fileUrl" :href="selectedWork.fileUrl" target="_blank" type="primary">
                  {{ selectedWork.fileUrl }}
                </el-link>
                <span v-else>-</span>
              </el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>

          <el-tab-pane label="模型/描述" name="model">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="创作类型">{{ formatCreationType(selectedWork.creationType) }}</el-descriptions-item>
              <el-descriptions-item label="模型名称">{{ selectedWork.modelName || '-' }}</el-descriptions-item>
              <el-descriptions-item label="模型版本">{{ selectedWork.modelVersion || '-' }}</el-descriptions-item>
              <el-descriptions-item label="模型来源">{{ selectedWork.modelSource || '-' }}</el-descriptions-item>
              <el-descriptions-item label="摘要" :span="2">
                <div class="scroll-box pre">{{ selectedWork.summary || '-' }}</div>
              </el-descriptions-item>
              <el-descriptions-item label="生成提示词" :span="2">
                <div class="scroll-box pre">{{ selectedWork.prompt || '-' }}</div>
              </el-descriptions-item>
              <el-descriptions-item label="模型参数" :span="2">
                <div class="scroll-box">
                  <el-table v-if="modelParamsRows.length" :data="modelParamsRows" size="small" border style="width: 100%">
                    <el-table-column prop="key" label="参数" width="200" />
                    <el-table-column prop="value" label="值" />
                  </el-table>
                  <el-empty v-else description="无模型参数" />
                </div>
              </el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
        </el-tabs>
        <el-empty v-else-if="!detailLoading" description="暂无作品详情" />
      </div>
    </el-dialog>

    <!-- Transfer Dialog -->
    <el-dialog v-model="transferVisible" title="版权转让" width="600px">
      <el-form :model="transferForm" label-width="140px">
        <el-form-item label="受让人">
          <el-input v-model="recipientKeyword" placeholder="输入对方手机号或邮箱">
            <template #append>
              <el-button :loading="lookupLoading" @click="handleLookupUser">查询</el-button>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item v-if="lookupTried" label="查询结果">
          <el-card v-if="lookupUser" class="lookup-card" shadow="never">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="用户名">{{ lookupUser.username }}</el-descriptions-item>
              <el-descriptions-item label="手机号">{{ lookupUser.phone || '-' }}</el-descriptions-item>
              <el-descriptions-item label="邮箱" :span="2">{{ lookupUser.email || '-' }}</el-descriptions-item>
            </el-descriptions>
            <div class="lookup-actions">
              <el-checkbox v-model="recipientChecked" :disabled="isSelfRecipient">选择该用户</el-checkbox>
              <span v-if="isSelfRecipient" class="lookup-hint">不能选择自己</span>
            </div>
          </el-card>
          <el-empty v-else description="找不到对应用户" />
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
import { ref, onMounted, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { getMyWorks, certifyWork as certifyWorkApi, getWorkById } from '@/api/works'
import { lookupUserByContact } from '@/api/auth'
import { createTransfer } from '@/api/transfers'
import type { Work } from '@/types/work'
import type { UserLookup } from '@/types/user'
import dayjs from 'dayjs'

const userStore = useUserStore()

const loading = ref(false)
const works = ref<Work[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const selectedWork = ref<Work | null>(null)
const detailLoading = ref(false)
const detailTab = ref('base')
const transferVisible = ref(false)
const transfering = ref(false)
const recipientKeyword = ref('')
const lookupLoading = ref(false)
const lookupTried = ref(false)
const lookupUser = ref<UserLookup | null>(null)
const recipientChecked = ref(false)

const transferForm = ref({
  workId: '',
  toUserId: 0,
  transferType: 'FULL_TRANSFER' as 'FULL_TRANSFER' | 'LICENSE_GRANT',
  licenseType: undefined as 'PERSONAL' | 'COMMERCIAL' | 'EXCLUSIVE' | undefined,
  tradeDesc: ''
})

const currentUserId = computed(() => {
  const idFromStore = userStore.user?.id
  if (typeof idFromStore === 'number') return idFromStore
  const id = Number(localStorage.getItem('userId') || 0)
  return Number.isFinite(id) ? id : 0
})

const isSelfRecipient = computed(() => {
  return !!lookupUser.value?.id && lookupUser.value.id === currentUserId.value
})

const getStatusType = (status: string) => {
  const types: Record<string, any> = {
    'INIT': 'info',
    'UPLOADED': 'info',
    'CERTIFIED': 'success',
    'TRANSFERRED': 'warning',
    'OFFLINE': 'danger'
  }
  return types[status] || 'info'
}

const getAuditStatusType = (status: string) => {
  const map: any = {
    PENDING: 'warning',
    PASS: 'success',
    REJECT: 'danger'
  }
  return map[status] || 'info'
}

const formatAuditStatus = (status: string) => {
  const map: any = {
    PENDING: '待审核',
    PASS: '通过',
    REJECT: '拒绝'
  }
  return map[status] || status
}

const formatFileType = (fileType: Work['fileType']) => {
  const map: Record<Work['fileType'], string> = {
    TEXT: '文本',
    IMAGE: '图片',
    AUDIO: '音频',
    VIDEO: '视频',
    OTHER: '其他'
  }
  return map[fileType] || fileType
}

const formatWorkStatus = (status: Work['workStatus']) => {
  const map: Record<Work['workStatus'], string> = {
    INIT: '初始化',
    UPLOADED: '已上传',
    CERTIFIED: '已确权',
    OFFLINE: '已下线',
    TRANSFERRED: '已转让'
  }
  return map[status] || status
}

const formatRightType = (rightType: Work['rightType']) => {
  const map: Record<Work['rightType'], string> = {
    RIGHT_OWNERSHIP: '著作权/所有权',
    RIGHT_USAGE: '使用权'
  }
  return map[rightType] || rightType
}

const formatLicenseType = (licenseType: Work['licenseType']) => {
  const map: Record<Work['licenseType'], string> = {
    PERSONAL: '个人使用',
    COMMERCIAL: '商业使用',
    EXCLUSIVE: '独占许可'
  }
  return map[licenseType] || licenseType
}

const formatCreationType = (creationType?: Work['creationType']) => {
  if (!creationType) return '-'
  const map: Record<string, string> = {
    AI_GENERATED: 'AI 生成',
    HUMAN_AI_COLLAB: '人机协作',
    AI_ASSISTED: 'AI 辅助'
  }
  return map[creationType] || creationType
}

const formatDateTime = (value: any) => {
  if (!value) return '-'
  const d = dayjs(value)
  if (!d.isValid()) return String(value)
  return d.format('YYYY-MM-DD HH:mm:ss')
}

const formatSize = (size: number) => {
  if (size < 1024) return size + ' B'
  if (size < 1024 * 1024) return (size / 1024).toFixed(2) + ' KB'
  return (size / 1024 / 1024).toFixed(2) + ' MB'
}

const modelParamsRows = computed(() => {
  const params = selectedWork.value?.modelParams
  if (!params || typeof params !== 'object') return []
  return Object.entries(params).map(([key, value]) => ({
    key,
    value: typeof value === 'string' ? value : JSON.stringify(value)
  }))
})

const handleLookupUser = async () => {
  const keyword = recipientKeyword.value.trim()
  if (!keyword) {
    ElMessage.warning('请输入手机号或邮箱')
    return
  }
  lookupLoading.value = true
  lookupTried.value = true
  lookupUser.value = null
  recipientChecked.value = false
  transferForm.value.toUserId = 0
  try {
    const response = await lookupUserByContact(keyword)
    lookupUser.value = response.data
  } catch (error: any) {
    ElMessage.error('查询用户失败：' + (error?.message || '未知错误'))
  } finally {
    lookupLoading.value = false
  }
}

watch(recipientChecked, (checked) => {
  if (!lookupUser.value || isSelfRecipient.value) {
    transferForm.value.toUserId = 0
    return
  }
  transferForm.value.toUserId = checked ? lookupUser.value.id : 0
})

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

const viewWork = async (work: Work) => {
  detailVisible.value = true
  detailLoading.value = true
  selectedWork.value = null
  try {
    const response = await getWorkById(work.id)
    selectedWork.value = response.data
  } catch (error: any) {
    ElMessage.error('获取作品详情失败：' + (error?.message || '未知错误'))
    detailVisible.value = false
  } finally {
    detailLoading.value = false
  }
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
  
  transferForm.value.workId = work.workId
  transferForm.value.toUserId = 0
  transferForm.value.tradeDesc = ''
  recipientKeyword.value = ''
  lookupTried.value = false
  lookupUser.value = null
  recipientChecked.value = false
  transferVisible.value = true
}

const handleTransfer = async () => {
  if (!transferForm.value.toUserId) {
    ElMessage.warning('请先查询并勾选受让人')
    return
  }
  if (transferForm.value.toUserId === currentUserId.value) {
    ElMessage.warning('不能转让给自己')
    return
  }
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
  padding: 10px 0;
}

.mono {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
}

.wrap {
  word-break: break-all;
}

.pre {
  white-space: pre-wrap;
  word-break: break-word;
  color: rgba(15, 23, 42, 0.78);
}

.work-detail-dialog :deep(.el-dialog) {
  margin-left: auto;
  margin-right: auto;
}

.work-detail-dialog :deep(.el-dialog__body) {
  padding-top: 12px;
  padding-bottom: 14px;
  max-height: 64vh;
  overflow: auto;
}

.work-detail-dialog :deep(.el-descriptions__label),
.work-detail-dialog :deep(.el-descriptions__content) {
  padding: 10px 12px;
}

.work-detail-dialog :deep(.el-tag) {
  border-radius: 10px;
}

.detail-tabs :deep(.el-tabs__header) {
  margin: 0 0 12px;
}

.scroll-box {
  max-height: 160px;
  overflow: auto;
}

.lookup-card {
  width: 100%;
}

.lookup-actions {
  margin-top: 10px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.lookup-hint {
  font-size: 12px;
  color: rgba(15, 23, 42, 0.6);
}

@media (min-width: 960px) {
  .work-detail-dialog :deep(.el-dialog) {
    transform: translateX(134px);
  }
}

@media (max-width: 959px) {
  .work-detail-dialog :deep(.el-dialog) {
    transform: none;
  }
}
</style>
