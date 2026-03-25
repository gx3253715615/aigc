<template>
  <el-dialog v-model="dialogVisible" title="个人信息" width="560px" :close-on-click-modal="false">
    <div v-loading="loading">
      <div class="header">
        <el-avatar :size="64" :src="avatarSrc" :icon="UserFilled" />
        <div class="header-meta">
          <div class="name">{{ form.username || '用户' }}</div>
          <div class="sub">
            <el-tag v-if="form.authStatus === 'INIT'" type="warning" size="small">未认证</el-tag>
            <el-tag v-else-if="form.authStatus === 'AUTH'" type="success" size="small">已认证</el-tag>
          </div>
        </div>
        <el-upload
          class="avatar-uploader"
          :show-file-list="false"
          :auto-upload="false"
          accept="image/*"
          :on-change="handleAvatarChange"
        >
          <el-button>更换头像</el-button>
        </el-upload>
      </div>

      <el-form :model="form" label-position="top" class="form">
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="用户名">
              <el-input v-model="form.username" placeholder="请输入用户名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="钱包地址">
              <el-input v-model="form.walletAddress" readonly placeholder="未生成钱包地址" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="手机号">
              <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <template #footer>
      <div class="footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存修改</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue'
import { ElMessage, type UploadFile } from 'element-plus'
import { UserFilled } from '@element-plus/icons-vue'
import { getUserInfo, updateUser, uploadUserAvatar } from '@/api/auth'
import { useUserStore } from '@/stores/user'
import type { User } from '@/types/user'

interface Props {
  modelValue: boolean
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'updated'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const userStore = useUserStore()
const loading = ref(false)
const saving = ref(false)

const form = reactive<User>({
  id: 0,
  username: '',
  phone: '',
  email: '',
  avatarPath: '',
  avatarUrl: '',
  authStatus: 'INIT',
  status: 'ENABLE',
  walletAddress: ''
})

const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const avatarSrc = computed(() => {
  return form.avatarUrl || userStore.user?.avatarUrl || ''
})

const loadProfile = async () => {
  loading.value = true
  try {
    const response = await getUserInfo()
    Object.assign(form, response.data)
  } catch (error: any) {
    ElMessage.error('获取用户信息失败：' + (error?.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

watch(
  () => dialogVisible.value,
  (visible) => {
    if (visible) loadProfile()
  }
)

const handleAvatarChange = async (file: UploadFile) => {
  const raw = file.raw
  if (!raw) return
  if (!raw.type.startsWith('image/')) {
    ElMessage.warning('请选择图片文件')
    return
  }
  if (raw.size > 2 * 1024 * 1024) {
    ElMessage.warning('图片不能超过 2MB')
    return
  }

  if (!form.id) {
    ElMessage.error('用户信息异常，请重新登录')
    return
  }

  saving.value = true
  try {
    const response = await uploadUserAvatar(raw)
    form.avatarPath = response.data.avatarPath
    form.avatarUrl = response.data.avatarUrl

    await updateUser({
      id: form.id,
      avatarPath: form.avatarPath
    })
    await userStore.checkLoginStatus()
    ElMessage.success('头像已更新')
    emit('updated')
  } catch (error: any) {
    ElMessage.error('头像更新失败：' + (error?.message || '未知错误'))
  } finally {
    saving.value = false
  }
}

const save = async () => {
  if (!form.id) {
    ElMessage.error('用户信息异常，请重新登录')
    return
  }

  saving.value = true
  try {
    await updateUser({
      id: form.id,
      username: form.username,
      phone: form.phone,
      email: form.email,
      avatarPath: form.avatarPath
    })
    await userStore.checkLoginStatus()
    if (userStore.user?.username) {
      localStorage.setItem('username', userStore.user.username)
    }
    ElMessage.success('保存成功')
    emit('updated')
    dialogVisible.value = false
  } catch (error: any) {
    ElMessage.error('保存失败：' + (error?.message || '未知错误'))
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 6px 0 10px;
}

.header-meta {
  flex: 1;
  min-width: 0;
}

.name {
  font-size: 16px;
  font-weight: 750;
  color: rgba(15, 23, 42, 0.92);
}

.sub {
  margin-top: 6px;
}

.avatar-uploader {
  flex: none;
}

.form {
  margin-top: 8px;
}

.footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
