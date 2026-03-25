<template>
  <DefaultLayout>
    <template #header-left>
      <div class="page-title">
        <div class="title">作品详情</div>
        <div class="subtitle">查看确权信息与链上状态</div>
      </div>
    </template>
    <template #header-actions>
      <el-button @click="handleBack">返回</el-button>
      <el-button type="primary" @click="handleTransfer">转让</el-button>
    </template>

    <el-card v-loading="loading" class="panel" shadow="never">
      <div v-if="work" class="work-detail">
        <div class="work-header">
          <h2>{{ work.title }}</h2>
          <el-tag :type="getWorkStatusType(work.status)">{{ work.status }}</el-tag>
        </div>

        <div class="work-info">
          <el-descriptions title="基本信息" :column="2" border>
            <el-descriptions-item label="作品 ID">{{ work.workId }}</el-descriptions-item>
            <el-descriptions-item label="作者">{{ work.authorName }}</el-descriptions-item>
            <el-descriptions-item label="文件类型">{{ work.fileType }}</el-descriptions-item>
            <el-descriptions-item label="许可类型">{{ work.licenseType }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ work.createTime }}</el-descriptions-item>
            <el-descriptions-item label="链上状态">{{ work.chainStatus }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="work-content">
          <h3>作品描述</h3>
          <p>{{ work.description || '暂无描述' }}</p>
        </div>
      </div>
    </el-card>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import DefaultLayout from '@/layouts/DefaultLayout.vue'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
type WorkDetail = {
  workId: string
  title: string
  description?: string
  authorName: string
  fileType: string
  licenseType: string
  status: string
  chainStatus: string
  createTime: string
}

const work = ref<WorkDetail | null>(null)

const getWorkStatusType = (status: string) => {
  return status === '活跃' ? 'success' : 'info'
}

const loadWorkDetail = async () => {
  loading.value = true
  try {
    // TODO: Implement API call to get work detail
    // For now, using mock data
    work.value = {
      workId: route.params.id as string,
      title: '示例作品',
      description: '这是一个示例作品的描述信息',
      authorName: '张三',
      fileType: '图片',
      licenseType: '独占许可',
      status: '活跃',
      chainStatus: '已验证',
      createTime: new Date().toISOString()
    }
  } catch (error: any) {
    ElMessage.error('加载作品详情失败')
  } finally {
    loading.value = false
  }
}

const handleBack = () => {
  router.push('/works')
}

const handleTransfer = () => {
  router.push(`/transfers?workId=${work.value?.workId}`)
}

onMounted(() => {
  loadWorkDetail()
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

.work-detail {
  max-width: 1200px;
  margin: 0 auto;
}

.work-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.work-header h2 {
  font-size: 28px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0;
}

.work-info {
  margin-bottom: 32px;
}

.work-content {
  margin-bottom: 32px;
}

.work-content h3 {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 12px;
}

.work-content p {
  font-size: 14px;
  color: #5a6a7a;
  line-height: 1.6;
}
</style>
