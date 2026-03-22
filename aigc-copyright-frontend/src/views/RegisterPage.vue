<template>
  <div class="auth-shell">
    <div class="auth-card">
      <div class="auth-aside">
        <div class="brand">
          <div class="brand-mark">
            <el-icon :size="20"><Lock /></el-icon>
          </div>
          <div class="brand-text">
            <div class="brand-title">AIGC 版权保护</div>
            <div class="brand-subtitle">创建账户后即可上传与管理作品</div>
          </div>
        </div>

        <div class="feature">
          <div class="feature-title">注册后你可以</div>
          <ul class="feature-list">
            <li>上传作品并生成哈希摘要</li>
            <li>完成实名认证后进行确权</li>
            <li>发起版权转让并查看记录</li>
          </ul>
        </div>
      </div>

      <div class="auth-main">
        <div class="register-header">
          <h1>创建账户</h1>
          <p>加入 AIGC 版权保护系统</p>
        </div>

        <el-form
          ref="registerFormRef"
          :model="registerForm"
          :rules="registerRules"
          class="register-form"
        >
          <el-form-item prop="username">
            <el-input
              v-model="registerForm.username"
              placeholder="用户名"
              size="large"
              :prefix-icon="User"
            />
          </el-form-item>

          <el-form-item prop="email">
            <el-input
              v-model="registerForm.email"
              placeholder="邮箱（选填）"
              size="large"
              :prefix-icon="Message"
            />
          </el-form-item>

          <el-form-item prop="phone">
            <el-input
              v-model="registerForm.phone"
              placeholder="手机号（选填）"
              size="large"
              :prefix-icon="Phone"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="密码"
              size="large"
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>

          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="确认密码"
              size="large"
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              size="large"
              class="register-button"
              :loading="loading"
              @click="handleRegister"
            >
              注册
            </el-button>
          </el-form-item>
        </el-form>

        <div class="register-footer">
          <span>已有账户？</span>
          <el-button link type="primary" @click="goToLogin">去登录</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { Lock, User, Message, Phone } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const registerFormRef = ref<FormInstance>()
const loading = ref(false)

const registerForm = reactive({
  username: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

const validatePassword = (_rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请确认密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const registerRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, message: '用户名长度至少为 3 位', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少为 6 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validatePassword, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const result = await userStore.register({
          username: registerForm.username,
          email: registerForm.email || undefined,
          phone: registerForm.phone || undefined,
          password: registerForm.password
        })
        
        if (result.success) {
          ElMessage.success('注册成功！请登录')
          router.push('/login')
        } else {
          ElMessage.error(result.message || '注册失败')
        }
      } catch (error: any) {
        ElMessage.error(error.message || '注册失败')
      } finally {
        loading.value = false
      }
    }
  })
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.auth-shell {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 22px;
  background: radial-gradient(900px 520px at 18% 10%, rgba(79, 70, 229, 0.45), rgba(79, 70, 229, 0) 60%),
    radial-gradient(900px 520px at 82% 18%, rgba(34, 197, 94, 0.3), rgba(34, 197, 94, 0) 62%),
    linear-gradient(180deg, #0b1220, #0f172a 45%, #111827);
}

.auth-card {
  width: 100%;
  max-width: 980px;
  display: flex;
  border-radius: 22px;
  border: 1px solid rgba(255, 255, 255, 0.12);
  background: rgba(255, 255, 255, 0.08);
  box-shadow: 0 24px 80px rgba(0, 0, 0, 0.35);
  overflow: hidden;
  backdrop-filter: blur(14px);
}

.auth-aside {
  flex: 1;
  padding: 34px 32px;
  border-right: 1px solid rgba(255, 255, 255, 0.12);
  color: rgba(255, 255, 255, 0.86);
  background: radial-gradient(900px 520px at 20% 20%, rgba(255, 255, 255, 0.12), rgba(255, 255, 255, 0));
}

.auth-main {
  flex: 1;
  padding: 40px 38px;
  background: rgba(255, 255, 255, 0.92);
}

.register-header {
  margin-bottom: 22px;
}

.register-header h1 {
  font-size: 26px;
  font-weight: 800;
  color: rgba(15, 23, 42, 0.92);
  margin: 0 0 8px 0;
}

.register-header p {
  font-size: 14px;
  color: rgba(15, 23, 42, 0.62);
  margin: 0;
}

.register-form {
  margin-top: 24px;
}

.register-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 14px;
  margin-top: 12px;
}

.register-footer {
  text-align: center;
  margin-top: 24px;
  color: rgba(15, 23, 42, 0.62);
  font-size: 14px;
}

.register-footer span {
  margin-right: 8px;
}

:deep(.el-input__wrapper) {
  border-radius: 14px;
  padding: 12px 14px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
}

.brand-mark {
  width: 40px;
  height: 40px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: radial-gradient(80% 80% at 30% 30%, rgba(79, 70, 229, 0.95), rgba(79, 70, 229, 0.25));
  border: 1px solid rgba(255, 255, 255, 0.16);
}

.brand-title {
  font-size: 15px;
  font-weight: 800;
  letter-spacing: 0.2px;
}

.brand-subtitle {
  margin-top: 2px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.64);
}

.feature {
  margin-top: 26px;
}

.feature-title {
  font-size: 13px;
  font-weight: 750;
  color: rgba(255, 255, 255, 0.88);
  margin-bottom: 10px;
}

.feature-list {
  margin: 0;
  padding-left: 18px;
  color: rgba(255, 255, 255, 0.72);
  font-size: 13px;
  line-height: 1.7;
}

@media (max-width: 900px) {
  .auth-card {
    max-width: 520px;
  }

  .auth-aside {
    display: none;
  }

  .auth-main {
    padding: 42px 28px;
  }
}
</style>
