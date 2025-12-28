<template>
  <div class="dashboard-layout">
    <aside class="sidebar">
      <div class="sidebar-header"><h2>AIGC Copyright</h2></div>
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
      <header class="header"><h1>Upload Work</h1></header>

      <div class="upload-content">
        <el-card class="upload-card">
          <el-upload
            ref="uploadRef"
            class="upload-area"
            drag
            :auto-upload="false"
            :limit="1"
            :on-change="handleFileChange"
            :on-exceed="handleExceed"
          >
            <el-icon class="upload-icon"><UploadFilled /></el-icon>
            <div class="upload-text">Drop file here or <em>click to upload</em></div>
            <template #tip>
              <div class="upload-tip">Support for text, image, audio, video files (max 100MB)</div>
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
            <el-form-item label="File Name">
              <el-input v-model="uploadForm.fileName" disabled />
            </el-form-item>
            <el-form-item label="Summary">
              <el-input v-model="uploadForm.summary" type="textarea" :rows="3" placeholder="Brief description of the work" />
            </el-form-item>
            <el-form-item label="AI Model Name">
              <el-input v-model="uploadForm.modelName" placeholder="e.g., GPT-4, DALL-E, etc." />
            </el-form-item>
            <el-form-item label="Model Version">
              <el-input v-model="uploadForm.modelVersion" placeholder="e.g., v1.0" />
            </el-form-item>
            <el-form-item label="Model Source">
              <el-input v-model="uploadForm.modelSource" placeholder="e.g., OpenAI, Midjourney, etc." />
            </el-form-item>
            
            <el-form-item label="Model Parameters">
              <div class="model-params-container">
                <div v-for="(value, key, index) in uploadForm.modelParams" :key="index" class="param-row">
                  <el-input v-model="uploadForm.modelParams[key].name" @blur="updateParamKey(key, uploadForm.modelParams[key].name)" placeholder="Parameter name" style="width: 40%" />
                  <el-input v-model="uploadForm.modelParams[key].value" placeholder="Value" style="width: 45%; margin-left: 8px" />
                  <el-button type="danger" :icon="Delete" circle @click="removeParam(key)" style="margin-left: 8px" />
                </div>
                <el-button type="primary" plain :icon="Plus" @click="addParam" style="margin-top: 8px">Add Parameter</el-button>
              </div>
            </el-form-item>
            
            <el-form-item label="Prompt">
              <el-input v-model="uploadForm.prompt" type="textarea" :rows="4" placeholder="The prompt used to generate this work" />
            </el-form-item>
            <el-form-item label="Creation Type">
              <el-select v-model="uploadForm.creationType" placeholder="Select creation type">
                <el-option label="AI Generated" value="AI_GENERATED" />
                <el-option label="Human-AI Collaboration" value="HUMAN_AI_COLLAB" />
                <el-option label="AI Assisted" value="AI_ASSISTED" />
              </el-select>
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" size="large" :loading="uploading" @click="handleUpload">
                <el-icon><Upload /></el-icon>
                Upload Work
              </el-button>
              <el-button size="large" @click="handleReset">Reset</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, type UploadInstance, type UploadProps, type UploadRawFile } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { uploadWork } from '@/api/works'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
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

const handleMenuSelect = (index: string) => router.push(index)
const handleLogout = () => {
  userStore.logout()
  router.push('/login')
  ElMessage.success('Logout successful')
}

const handleFileChange: UploadProps['onChange'] = (uploadFile, uploadFiles) => {
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
    ElMessage.warning('Please select a file')
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
    ElMessage.success('Work uploaded successfully!')
    handleReset()
    router.push('/works')
  } catch (error: any) {
    ElMessage.error('Upload failed: ' + error.message)
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
@import './DashboardPage.vue';
.upload-content {
  padding: 32px;
}
.upload-card {
  max-width: 900px;
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
  margin-bottom: 8px;
}
:deep(.el-upload-dragger) {
  padding: 60px 20px;
}
</style>
