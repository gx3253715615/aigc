<template>
  <DefaultLayout>
    <template #header-left>
      <div class="page-title">
        <div class="title">区块链查询</div>
        <div class="subtitle">查询证书并验证文件哈希</div>
      </div>
    </template>

    <el-row :gutter="16" class="grid">
      <el-col :xs="24" :md="12">
        <el-card header="查询版权证书" class="panel" shadow="never">
            <el-form @submit.prevent="queryCertificate">
              <el-form-item label="作品 ID">
                <el-input v-model="certWorkId" placeholder="请输入作品 ID" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="queryCertificate" :loading="certLoading">查询</el-button>
              </el-form-item>
            </el-form>
            <div v-if="certInfo" class="result-box">
              <el-descriptions title="证书信息" :column="1" border>
                <el-descriptions-item label="作品 ID">{{ certInfo.workId }}</el-descriptions-item>
                <el-descriptions-item label="是否存在">
                  <el-tag :type="certInfo.exists ? 'success' : 'danger'">
                    {{ certInfo.exists ? '是' : '否' }}
                  </el-tag>
                </el-descriptions-item>
              </el-descriptions>
            </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :md="12">
        <el-card header="验证版权" class="panel" shadow="never">
            <el-form @submit.prevent="verifyCopyright">
              <el-form-item label="作品 ID">
                <el-input v-model="verifyWorkId" placeholder="请输入作品 ID" />
              </el-form-item>
              <el-form-item label="文件哈希">
                <el-input v-model="verifyHash" placeholder="请输入文件哈希" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="verifyCopyright" :loading="verifyLoading">验证</el-button>
              </el-form-item>
            </el-form>
            <div v-if="verifyResult !== null" class="result-box">
              <el-result
                :icon="verifyResult ? 'success' : 'error'"
                :title="verifyResult ? '验证成功' : '验证失败'"
                :sub-title="verifyResult ? '该版权已在区块链上存证' : '未找到版权记录或版权无效'"
              />
            </div>
        </el-card>
      </el-col>
    </el-row>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { getCertificate, verifyCopyright as verifyApi } from '@/api/blockchain'
import type { CertificateInfo } from '@/api/blockchain'

const certWorkId = ref('')
const certLoading = ref(false)
const certInfo = ref<CertificateInfo | null>(null)

const verifyWorkId = ref('')
const verifyHash = ref('')
const verifyLoading = ref(false)
const verifyResult = ref<boolean | null>(null)

const queryCertificate = async () => {
  if (!certWorkId.value) {
    ElMessage.warning('请输入作品 ID')
    return
  }
  certLoading.value = true
  try {
    const response = await getCertificate(certWorkId.value)
    certInfo.value = response.data
  } catch (error: any) {
    ElMessage.error('查询失败：' + error.message)
  } finally {
    certLoading.value = false
  }
}

const verifyCopyright = async () => {
  if (!verifyWorkId.value || !verifyHash.value) {
    ElMessage.warning('请填写完整信息')
    return
  }
  verifyLoading.value = true
  try {
    const response = await verifyApi(verifyWorkId.value, verifyHash.value)
    verifyResult.value = response.data
  } catch (error: any) {
    ElMessage.error('验证失败：' + error.message)
  } finally {
    verifyLoading.value = false
  }
}
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

.grid {
  margin-top: 2px;
}

.panel {
  border: 1px solid rgba(2, 6, 23, 0.08);
  background: rgba(255, 255, 255, 0.72);
  backdrop-filter: blur(10px);
}

.result-box {
  margin-top: 24px;
}
</style>
