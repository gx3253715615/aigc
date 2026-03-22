<template>
  <DefaultLayout>
    <template #header-left>
      <div class="page-title">
        <div class="title">上传作品</div>
        <div class="subtitle">支持图片、音频、视频与文本等格式</div>
      </div>
    </template>

    <div class="upload-page">
      <div v-if="userStore.user?.authStatus === 'INIT'" class="warning-message">
        <el-alert
          title="注意：您尚未通过实名认证，无法上传作品"
          type="warning"
          show-icon
          :closable="false"
        />
      </div>

      <el-card class="upload-card panel" shadow="never">
        <el-upload
          ref="uploadRef"
          class="upload-area"
          drag
          :auto-upload="false"
          :limit="1"
          :on-change="handleFileChange"
          :on-exceed="handleExceed"
          :disabled="userStore.user?.authStatus !== 'AUTH'"
        >
          <el-icon class="upload-icon"><UploadFilled /></el-icon>
          <div class="upload-text">将文件拖到此处，或<em>点击上传</em></div>
          <template #tip>
            <div class="upload-tip">支持文本、图片、音频、视频等文件格式（最大 100MB）</div>
          </template>
        </el-upload>

        <el-form
          v-if="fileSelected"
          ref="formRef"
          :model="uploadForm"
          label-width="140px"
          class="upload-form"
        >
          <el-divider />
          <el-form-item label="文件名">
            <el-input v-model="uploadForm.fileName" disabled />
          </el-form-item>
          <el-form-item label="作品摘要">
            <el-input v-model="uploadForm.summary" type="textarea" :rows="3" placeholder="请简要描述您的作品" />
          </el-form-item>
          <el-form-item label="AI 模型名称">
            <el-input v-model="uploadForm.modelName" placeholder="例如：GPT-4、DALL-E 等" />
          </el-form-item>
          <el-form-item label="模型版本">
            <el-input v-model="uploadForm.modelVersion" placeholder="例如：v1.0" />
          </el-form-item>
          <el-form-item label="模型来源">
            <el-input v-model="uploadForm.modelSource" placeholder="例如：OpenAI、Midjourney 等" />
          </el-form-item>
          
          <el-form-item label="模型参数">
            <div class="model-params-container">
              <div v-for="(_value, key, index) in uploadForm.modelParams" :key="index" class="param-row">
                <el-input
                  v-model="uploadForm.modelParams[key].name"
                  class="param-key"
                  placeholder="参数名"
                  @blur="updateParamKey(key, uploadForm.modelParams[key].name)"
                />
                <el-input v-model="uploadForm.modelParams[key].value" class="param-value" placeholder="参数值" />
                <el-button type="danger" :icon="Delete" circle @click="removeParam(key)" />
              </div>
              <el-button type="primary" plain :icon="Plus" @click="addParam" class="add-param">添加参数</el-button>
            </div>
          </el-form-item>
          
          <el-form-item label="生成提示词">
            <el-input v-model="uploadForm.prompt" type="textarea" :rows="4" placeholder="请输入生成此作品使用的提示词" />
          </el-form-item>
          <el-form-item label="创作类型">
            <el-select v-model="uploadForm.creationType" placeholder="请选择创作类型">
              <el-option label="AI 生成" value="AI_GENERATED" />
              <el-option label="人机协作" value="HUMAN_AI_COLLAB" />
              <el-option label="AI 辅助" value="AI_ASSISTED" />
            </el-select>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" size="large" :loading="uploading" @click="handleUpload">
              <el-icon><Upload /></el-icon>
              上传作品
            </el-button>
            <el-button size="large" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type UploadInstance, type UploadProps, type UploadRawFile } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { uploadWork } from '@/api/works'

const router = useRouter()
const userStore = useUserStore()

const uploadRef = ref<UploadInstance>()
const formRef = ref()
const uploading = ref(false)
const fileSelected = ref(false)
const selectedFile = ref<File | null>(null)

const uploadForm = ref({
  fileName: '',
  summary: '',
  modelName: '',
  modelVersion: '',
  modelSource: '',
  modelParams: {} as Record<string, { name: string; value: string }>,
  prompt: '',
  creationType: ''
})

const handleFileChange: UploadProps['onChange'] = (uploadFile) => {
  if (uploadFile.raw) {
    selectedFile.value = uploadFile.raw
    uploadForm.value.fileName = uploadFile.name
    fileSelected.value = true
  }
}

const handleExceed: UploadProps['onExceed'] = (files) => {
  uploadRef.value!.clearFiles()
  const file = files[0] as UploadRawFile
  uploadRef.value!.handleStart(file)
}

const addParam = () => {
  const timestamp = Date.now()
  uploadForm.value.modelParams[`param_${timestamp}`] = { name: '', value: '' }
}

const removeParam = (key: string) => {
  delete uploadForm.value.modelParams[key]
}

const updateParamKey = (oldKey: string, newName: string) => {
  if (newName && newName !== oldKey) {
    const value = uploadForm.value.modelParams[oldKey].value
    delete uploadForm.value.modelParams[oldKey]
    uploadForm.value.modelParams[newName] = { name: newName, value }
  }
}

const handleUpload = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请选择文件')
    return
  }

  // Check if user is authenticated
  if (userStore.user?.authStatus !== 'AUTH') {
    ElMessage.warning('您尚未通过实名认证，无法上传作品')
    return
  }

  uploading.value = true
  try {
    const formData = new FormData()
    formData.append('file', selectedFile.value)
    formData.append('summary', uploadForm.value.summary)
    formData.append('modelName', uploadForm.value.modelName)
    formData.append('modelVersion', uploadForm.value.modelVersion)
    formData.append('modelSource', uploadForm.value.modelSource)
    
    // 将模型参数转换为简单的键值对对象
    if (Object.keys(uploadForm.value.modelParams).length > 0) {
      const paramsMap: Record<string, string> = {}
      Object.values(uploadForm.value.modelParams).forEach(param => {
        if (param.name && param.value) {
          paramsMap[param.name] = param.value
        }
      })
      if (Object.keys(paramsMap).length > 0) {
        formData.append('modelParams', JSON.stringify(paramsMap))
      }
    }
    
    formData.append('prompt', uploadForm.value.prompt)
    formData.append('creationType', uploadForm.value.creationType)

    await uploadWork(formData)
    ElMessage.success('作品上传成功!')
    handleReset()
    router.push('/works')
  } catch (error: any) {
    ElMessage.error('上传失败：' + error.message)
  } finally {
    uploading.value = false
  }
}

const handleReset = () => {
  uploadRef.value?.clearFiles()
  selectedFile.value = null
  fileSelected.value = false
  uploadForm.value = {
    fileName: '',
    summary: '',
    modelName: '',
    modelVersion: '',
    modelSource: '',
    modelParams: {},
    prompt: '',
    creationType: ''
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

.upload-page {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.panel {
  border: 1px solid rgba(2, 6, 23, 0.08);
  background: rgba(255, 255, 255, 0.72);
  backdrop-filter: blur(10px);
}

.upload-card {
  max-width: 980px;
  margin: 0 auto;
}

.upload-area {
  width: 100%;
}
.upload-icon {
  font-size: 80px;
  color: #409eff;
  margin-bottom: 16px;
}
.upload-text {
  font-size: 16px;
  color: #606266;
  margin-bottom: 8px;
}
.upload-text em {
  color: #409eff;
  font-style: normal;
}
.upload-tip {
  font-size: 14px;
  color: #909399;
  margin-top: 12px;
}
.upload-form {
  margin-top: 20px;
}
.model-params-container {
  width: 100%;
}
.param-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.param-key {
  flex: 2;
}

.param-value {
  flex: 2.5;
}

.add-param {
  margin-top: 8px;
}
.warning-message {
  max-width: 980px;
  margin: 0 auto;
  width: 100%;
}
:deep(.el-upload-dragger) {
  padding: 60px 20px;
  border-radius: 14px;
  border-color: rgba(2, 6, 23, 0.1);
}
</style>
