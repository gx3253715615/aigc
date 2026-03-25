<template>
  <DefaultLayout>
    <template #header-left>
      <div class="page-title">
        <div class="title">作品审核</div>
        <div class="subtitle">审核用户上传的作品信息</div>
      </div>
    </template>

    <el-card class="panel" shadow="never">
      <div class="toolbar">
        <el-select v-model="statusFilter" placeholder="审核状态" clearable @change="handleSearch" class="filter-select">
          <el-option label="待审核" value="PENDING" />
          <el-option label="通过" value="PASS" />
          <el-option label="拒绝" value="REJECT" />
        </el-select>
        <el-input v-model="keyword" placeholder="搜索作品ID/文件名" clearable class="search" @clear="handleSearch">
          <template #append>
            <el-button @click="handleSearch">查询</el-button>
          </template>
        </el-input>
      </div>

      <el-table :data="works" v-loading="loading" style="width: 100%">
        <el-table-column prop="workId" label="作品ID" min-width="120" />
        <el-table-column prop="fileName" label="文件名" min-width="150" show-overflow-tooltip />
        <el-table-column prop="userName" label="上传者" width="120" />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag size="small" type="info">{{ formatFileType(row.fileType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="审核状态" width="110">
          <template #default="{ row }">
            <el-tag :type="getAuditStatusType(row.status)">
              {{ formatAuditStatus(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="上传时间" width="170">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="viewWork(row)">查看</el-button>
            <el-button
              v-if="row.status === 'PENDING'"
              size="small"
              type="success"
              @click="handleAudit(row.id, 'PASS')"
            >
              通过
            </el-button>
            <el-button
              v-if="row.status === 'PENDING'"
              size="small"
              type="danger"
              @click="handleAudit(row.id, 'REJECT')"
            >
              拒绝
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
          layout="total, sizes, prev, pager, next"
          @size-change="handleSearch"
          @current-change="loadWorks"
        />
      </div>
    </el-card>

    <!-- 作品详情对话框 -->
    <el-dialog v-model="detailVisible" title="作品详情" width="800px" destroy-on-close>
      <div v-if="selectedWork" class="detail-container">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="作品ID">{{ selectedWork.workId }}</el-descriptions-item>
          <el-descriptions-item label="文件名">{{ selectedWork.fileName }}</el-descriptions-item>
          <el-descriptions-item label="文件类型">{{ formatFileType(selectedWork.fileType) }}</el-descriptions-item>
          <el-descriptions-item label="文件大小">{{ formatSize(selectedWork.fileSize) }}</el-descriptions-item>
          <el-descriptions-item label="上传者">{{ selectedWork.userName }}</el-descriptions-item>
          <el-descriptions-item label="上传时间">{{ formatDateTime(selectedWork.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="审核状态">
            <el-tag :type="getAuditStatusType(selectedWork.status)">{{ formatAuditStatus(selectedWork.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="模型名称">{{ selectedWork.modelName || '-' }}</el-descriptions-item>
        </el-descriptions>
        
        <div class="detail-section">
          <div class="section-title">作品摘要</div>
          <div class="section-content">{{ selectedWork.summary || '无摘要' }}</div>
        </div>

        <div class="detail-section">
          <div class="section-title">提示词 (Prompt)</div>
          <div class="section-content prompt-box">{{ selectedWork.prompt || '无提示词' }}</div>
        </div>

        <div class="detail-section" v-if="selectedWork.fileUrl">
          <div class="section-title">文件预览</div>
          <div class="preview-container">
            <el-image 
              v-if="selectedWork.fileType === 'IMAGE'" 
              :src="selectedWork.fileUrl" 
              fit="contain"
              class="preview-image"
              :preview-src-list="[selectedWork.fileUrl]"
            />
            <div v-else class="file-link">
              <el-link :href="selectedWork.fileUrl" target="_blank" type="primary">点击查看/下载文件</el-link>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { getAdminWorks, auditWork } from '@/api/admin'
import type { Work } from '@/types/work'

const loading = ref(false)
const works = ref<Work[]>([])
const keyword = ref('')
const statusFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const detailVisible = ref(false)
const selectedWork = ref<Work | null>(null)

const loadWorks = async () => {
  loading.value = true
  try {
    const response = await getAdminWorks(
      currentPage.value, 
      pageSize.value, 
      keyword.value.trim() || undefined,
      statusFilter.value || undefined
    )
    works.value = response.data.records
    total.value = response.data.totalRow
  } catch (error: any) {
    ElMessage.error('加载作品列表失败：' + (error?.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadWorks()
}

const handleAudit = (id: number, status: 'PASS' | 'REJECT') => {
  const actionText = status === 'PASS' ? '通过' : '拒绝'
  ElMessageBox.confirm(`确定要${actionText}该作品吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: status === 'PASS' ? 'success' : 'warning'
  }).then(async () => {
    try {
      await auditWork(id, status)
      ElMessage.success('操作成功')
      loadWorks()
    } catch (error: any) {
      ElMessage.error('操作失败：' + (error?.message || '未知错误'))
    }
  }).catch(() => {})
}

const viewWork = (work: Work) => {
  selectedWork.value = work
  detailVisible.value = true
}

const formatFileType = (fileType: string) => {
  const map: any = {
    TEXT: '文本',
    IMAGE: '图片',
    AUDIO: '音频',
    VIDEO: '视频',
    OTHER: '其他'
  }
  return map[fileType] || fileType
}

const formatAuditStatus = (status: string) => {
  const map: any = {
    PENDING: '待审核',
    PASS: '通过',
    REJECT: '拒绝'
  }
  return map[status] || status
}

const getAuditStatusType = (status: string) => {
  const map: any = {
    PENDING: 'warning',
    PASS: 'success',
    REJECT: 'danger'
  }
  return map[status] || 'info'
}

const formatDateTime = (value: any) => {
  if (!value) return '-'
  return dayjs(value).format('YYYY-MM-DD HH:mm:ss')
}

const formatSize = (size: number) => {
  if (size < 1024) return size + ' B'
  if (size < 1024 * 1024) return (size / 1024).toFixed(2) + ' KB'
  return (size / 1024 / 1024).toFixed(2) + ' MB'
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

.toolbar {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-bottom: 14px;
}

.filter-select {
  width: 120px;
}

.search {
  max-width: 320px;
}

.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

.detail-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.detail-section {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.section-title {
  font-weight: 700;
  font-size: 14px;
  color: #333;
  padding-left: 8px;
  border-left: 4px solid #409eff;
}

.section-content {
  padding: 12px;
  background: #f8fafc;
  border-radius: 4px;
  font-size: 14px;
  line-height: 1.6;
  color: #475569;
}

.prompt-box {
  white-space: pre-wrap;
  font-family: monospace;
}

.preview-container {
  display: flex;
  justify-content: center;
  background: #f1f5f9;
  padding: 20px;
  border-radius: 8px;
}

.preview-image {
  max-width: 100%;
  max-height: 400px;
}

.file-link {
  padding: 20px;
}
</style>
