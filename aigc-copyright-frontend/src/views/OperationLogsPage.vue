<template>
  <DefaultLayout>
    <template #header-left>
      <div class="page-title">
        <div class="title">操作日志</div>
        <div class="subtitle">{{ isAdmin ? '全站操作审计记录' : '你的个人操作历史记录' }}</div>
      </div>
    </template>

    <el-card class="panel" shadow="never">
      <div class="toolbar">
        <div class="filter-group">
          <el-select
            v-model="moduleFilter"
            placeholder="所属模块"
            clearable
            class="filter-item"
            @change="handleSearch"
          >
            <el-option label="全部模块" value="" />
            <el-option label="作品模块" value="作品" />
            <el-option label="用户模块" value="用户" />
            <el-option label="转让模块" value="转让" />
          </el-select>

          <el-input
            v-if="isAdmin"
            v-model="username"
            placeholder="搜索操作人"
            clearable
            class="filter-item search-input"
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>

          <el-input
            v-model="keyword"
            placeholder="搜索操作描述"
            clearable
            class="filter-item search-input"
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch">
                <el-icon><Search /></el-icon>
                查询
              </el-button>
            </template>
          </el-input>
        </div>
      </div>

      <el-table :data="logs" v-loading="loading" style="width: 100%">
        <el-table-column prop="createTime" label="操作时间" width="170">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column v-if="isAdmin" prop="username" label="操作人" width="120" />
        <el-table-column prop="module" label="模块" width="100" />
        <el-table-column prop="operationType" label="类型" width="90">
          <template #default="{ row }">
            <el-tag :type="getOperationTypeTag(row.operationType)" size="small">
              {{ formatOperationType(row.operationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="操作描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="ip" label="IP地址" width="130" />
        <el-table-column label="详情" width="80" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="showDetail(row)">详情</el-button>
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
          @current-change="loadLogs"
        />
      </div>
    </el-card>

    <!-- 日志详情对话框 -->
    <el-dialog v-model="detailVisible" title="日志详情" width="600px">
      <el-descriptions v-if="selectedLog" :column="1" border>
        <el-descriptions-item label="操作 ID">{{ selectedLog.id }}</el-descriptions-item>
        <el-descriptions-item label="操作用户">{{ selectedLog.username }} (ID: {{ selectedLog.userId }})</el-descriptions-item>
        <el-descriptions-item label="所属模块">{{ selectedLog.module }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">
          <el-tag :type="getOperationTypeTag(selectedLog.operationType)">
            {{ formatOperationType(selectedLog.operationType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="操作描述">{{ selectedLog.description }}</el-descriptions-item>
        <el-descriptions-item label="对象类型">{{ selectedLog.targetType || '-' }}</el-descriptions-item>
        <el-descriptions-item label="对象 ID">{{ selectedLog.targetId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="请求 URL">{{ selectedLog.requestUrl }}</el-descriptions-item>
        <el-descriptions-item label="请求方式">{{ selectedLog.requestMethod }}</el-descriptions-item>
        <el-descriptions-item label="操作 IP">{{ selectedLog.ip }}</el-descriptions-item>
        <el-descriptions-item label="浏览器代理">
          <div class="ua-text">{{ selectedLog.userAgent }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ formatDateTime(selectedLog.createTime) }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Search } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import request from '@/utils/request'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const isAdmin = computed(() => userStore.user?.isAdmin === 1)

const loading = ref(false)
const logs = ref<any[]>([])
const keyword = ref('')
const moduleFilter = ref('')
const username = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const detailVisible = ref(false)
const selectedLog = ref<any>(null)

const loadLogs = async () => {
  loading.value = true
  try {
    const res = await request.get('/logs/list', {
      params: {
        pageNum: currentPage.value,
        pageSize: pageSize.value,
        module: moduleFilter.value || undefined,
        username: isAdmin.value ? (username.value.trim() || undefined) : undefined,
        keyword: keyword.value.trim() || undefined
      }
    })
    logs.value = res.data.records
    total.value = res.data.totalRow
  } catch (error: any) {
    ElMessage.error('加载日志失败：' + (error.message || '网络错误'))
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadLogs()
}

const showDetail = (log: any) => {
  selectedLog.value = log
  detailVisible.value = true
}

const formatDateTime = (val: string) => {
  return val ? dayjs(val).format('YYYY-MM-DD HH:mm:ss') : '-'
}

const formatOperationType = (type: string) => {
  const map: any = {
    CREATE: '新增',
    UPDATE: '修改',
    DELETE: '删除'
  }
  return map[type] || type
}

const getOperationTypeTag = (type: string) => {
  const map: any = {
    CREATE: 'success',
    UPDATE: 'warning',
    DELETE: 'danger'
  }
  return map[type] || 'info'
}

onMounted(() => {
  loadLogs()
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
  margin-bottom: 16px;
}

.filter-group {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.filter-item {
  width: 160px;
}

.search-input {
  width: 240px;
}

.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

.ua-text {
  word-break: break-all;
  font-size: 12px;
  color: #64748b;
  line-height: 1.4;
}
</style>
