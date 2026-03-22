<template>
  <DefaultLayout>
    <template #header-left>
      <div class="page-title">
        <div class="title">转让记录</div>
        <div class="subtitle">查询作品的链上流转与状态</div>
      </div>
    </template>

    <el-card class="panel" shadow="never">
      <el-form inline class="search">
        <el-form-item label="作品 ID">
          <el-input v-model="searchWorkId" placeholder="请输入作品 ID" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadTransfers">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="transfers" v-loading="loading" style="width: 100%">
        <el-table-column prop="transferId" label="转让 ID" min-width="150" />
        <el-table-column prop="fromUserName" label="转出用户" width="120" />
        <el-table-column prop="toUserName" label="转入用户" width="120" />
        <el-table-column prop="transferType" label="转让类型" width="140" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.chainStatus)">{{ row.chainStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="转让时间" width="180" />
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="loadTransfers"
        />
      </div>
    </el-card>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { getTransferHistory } from '@/api/transfers'
import type { CopyrightTransfer } from '@/types/work'

const route = useRoute()
const loading = ref(false)
const searchWorkId = ref('')
const transfers = ref<CopyrightTransfer[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const getStatusType = (status: string) => {
  return status === 'SUCCESS' ? 'success' : status === 'PENDING' ? 'warning' : 'danger'
}

const loadTransfers = async () => {
  if (!searchWorkId.value) {
    ElMessage.warning('请输入作品 ID')
    return
  }
  loading.value = true
  try {
    const response = await getTransferHistory(searchWorkId.value, currentPage.value, pageSize.value)
    transfers.value = response.data.records
    total.value = response.data.totalRow
  } catch (error: any) {
    ElMessage.error('加载转让记录失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  const workId = route.query.workId
  if (typeof workId === 'string' && workId) {
    searchWorkId.value = workId
    loadTransfers()
  }
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

.search {
  margin-bottom: 16px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
