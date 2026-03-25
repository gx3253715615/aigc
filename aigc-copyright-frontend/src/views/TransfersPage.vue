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
        <el-form-item label="范围">
          <el-radio-group v-model="direction">
            <el-radio-button label="ALL">全部</el-radio-button>
            <el-radio-button label="OUT">我转出</el-radio-button>
            <el-radio-button label="IN">转入我</el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <el-table :data="transfers" v-loading="loading" style="width: 100%">
        <el-table-column prop="transferId" label="转让 ID" min-width="150" />
        <el-table-column prop="fromUserName" label="转出用户" width="120" />
        <el-table-column prop="toUserName" label="转入用户" width="120" />
        <el-table-column label="转让类型" width="140">
          <template #default="{ row }">
            {{ formatTransferType(row.transferType) }}
          </template>
        </el-table-column>
        <el-table-column width="140">
          <template #header>
            <div class="license-header">
              <span>许可类型</span>
              <el-popover placement="right" width="320" trigger="hover">
                <div class="license-popover">
                  <div class="license-item">
                    <div class="k">个人</div>
                    <div class="v">用于学习练习、个人项目、收藏展示、非盈利分享等非商业用途。</div>
                  </div>
                  <div class="license-item">
                    <div class="k">商用</div>
                    <div class="v">用于公司宣传材料、商业网站、产品设计、广告投放等商业场景。</div>
                  </div>
                  <div class="license-item">
                    <div class="k">独占</div>
                    <div class="v">购买独家使用权，避免他人同时使用；常见于品牌广告、出版发行等需要唯一性的场景。</div>
                  </div>
                </div>
                <template #reference>
                  <el-button link class="license-help" tabindex="-1">
                    <el-icon><QuestionFilled /></el-icon>
                  </el-button>
                </template>
              </el-popover>
            </div>
          </template>
          <template #default="{ row }">
            {{ formatLicenseType(row.transferType, row.licenseType) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.chainStatus)">{{ row.chainStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="转让时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openDetail(row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="handlePageSize"
          @current-change="loadTransfers"
        />
      </div>
    </el-card>

    <el-dialog v-model="detailVisible" title="交易详情" width="760px" top="7vh" append-to-body>
      <div v-loading="detailLoading" class="detail">
        <el-descriptions v-if="detail" :column="2" border>
          <el-descriptions-item label="转让 ID">{{ detail.transferId }}</el-descriptions-item>
          <el-descriptions-item label="作品 ID">{{ detail.workId }}</el-descriptions-item>
          <el-descriptions-item label="转出用户">{{ detail.fromUserName || detail.fromUserId }}</el-descriptions-item>
          <el-descriptions-item label="转入用户">{{ detail.toUserName || detail.toUserId }}</el-descriptions-item>
          <el-descriptions-item label="转让类型">
            {{ formatTransferType(detail.transferType) }}
          </el-descriptions-item>
          <el-descriptions-item label="许可类型">
            {{ formatLicenseType(detail.transferType, detail.licenseType) }}
          </el-descriptions-item>
          <el-descriptions-item label="转出地址" :span="2">
            <div class="mono wrap">{{ detail.fromAddress }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="转入地址" :span="2">
            <div class="mono wrap">{{ detail.toAddress }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="链上状态">
            <el-tag :type="getStatusType(detail.chainStatus)">{{ detail.chainStatus }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="业务状态">{{ detail.transferStatus }}</el-descriptions-item>
          <el-descriptions-item label="交易哈希" :span="2">
            <div class="mono wrap">{{ detail.chainTxHash || '-' }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="区块高度">{{ detail.blockNumber ?? '-' }}</el-descriptions-item>
          <el-descriptions-item label="链上时间">{{ formatDateTime(detail.blockTime) }}</el-descriptions-item>
          <el-descriptions-item label="生效时间">{{ formatDateTime(detail.effectiveTime) }}</el-descriptions-item>
          <el-descriptions-item label="到期时间">{{ formatDateTime(detail.expireTime) }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">
            <div class="pre">{{ detail.tradeDesc || '-' }}</div>
          </el-descriptions-item>
        </el-descriptions>
        <el-empty v-else-if="!detailLoading" description="暂无详情" />
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { getMyTransferHistory, getTransferById } from '@/api/transfers'
import type { CopyrightTransfer } from '@/types/work'
import dayjs from 'dayjs'
import { QuestionFilled } from '@element-plus/icons-vue'

const loading = ref(false)
const direction = ref<'ALL' | 'OUT' | 'IN'>('ALL')
const transfers = ref<CopyrightTransfer[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const detailLoading = ref(false)
const detail = ref<CopyrightTransfer | null>(null)

const getStatusType = (status: string) => {
  return status === 'SUCCESS' ? 'success' : status === 'PENDING' ? 'warning' : 'danger'
}

const formatTransferType = (t: string) => {
  const map: Record<string, string> = {
    FULL_TRANSFER: '全部转让',
    LICENSE_GRANT: '许可授权'
  }
  return map[t] || t
}

const formatLicenseType = (transferType: string, licenseType?: CopyrightTransfer['licenseType']) => {
  if (transferType !== 'LICENSE_GRANT') return '-'
  const map: Record<NonNullable<CopyrightTransfer['licenseType']>, string> = {
    PERSONAL: '个人',
    COMMERCIAL: '商用',
    EXCLUSIVE: '独占'
  }
  if (!licenseType) return '-'
  return map[licenseType] || licenseType
}

const formatDateTime = (value: any) => {
  if (!value) return '-'
  const d = dayjs(value)
  if (!d.isValid()) return String(value)
  return d.format('YYYY-MM-DD HH:mm:ss')
}

const loadTransfers = async () => {
  loading.value = true
  try {
    const response = await getMyTransferHistory(direction.value, undefined, currentPage.value, pageSize.value)
    transfers.value = response.data.records
    total.value = response.data.totalRow
  } catch (error: any) {
    ElMessage.error('加载转让记录失败：' + (error?.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

const openDetail = async (row: CopyrightTransfer) => {
  detailVisible.value = true
  detailLoading.value = true
  detail.value = null
  try {
    const response = await getTransferById(row.id)
    detail.value = response.data
  } catch (error: any) {
    ElMessage.error('加载交易详情失败：' + (error?.message || '未知错误'))
    detailVisible.value = false
  } finally {
    detailLoading.value = false
  }
}

const handlePageSize = () => {
  currentPage.value = 1
  loadTransfers()
}

onMounted(() => {
  loadTransfers()
})

watch(direction, () => {
  currentPage.value = 1
  loadTransfers()
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

.license-header {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.license-help {
  padding: 0;
  line-height: 1;
  color: rgba(15, 23, 42, 0.55);
}

.license-popover {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.license-item .k {
  font-weight: 700;
  color: rgba(15, 23, 42, 0.9);
  margin-bottom: 2px;
}

.license-item .v {
  font-size: 12px;
  color: rgba(15, 23, 42, 0.7);
  line-height: 1.5;
}

.detail {
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
</style>
