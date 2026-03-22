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
            <div class="brand-subtitle">链上确权 · 可追溯 · 可转让</div>
          </div>
        </div>

        <div class="feature">
          <div class="feature-title">为什么使用本系统</div>
          <ul class="feature-list">
            <li>上传作品后生成哈希并链上存证</li>
            <li>确权与转让流程可追溯、可验证</li>
            <li>统一管理作品、证书与流转记录</li>
          </ul>
        </div>
      </div>

      <div class="auth-main">
        <div class="login-header">
          <h1>欢迎回来</h1>
          <p>登录到您的账户</p>
        </div>

        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          @submit.prevent="handleLogin"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="用户名"
              size="large"
              :prefix-icon="User"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="密码"
              size="large"
              :prefix-icon="Lock"
              show-password
              @keyup.enter="handleLogin"
            />
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              size="large"
              class="login-button"
              :loading="loading"
              @click="handleLogin"
            >
              登录
            </el-button>
          </el-form-item>
        </el-form>

        <div class="login-footer">
          <span>还没有账户？</span>
          <el-button link type="primary" @click="goToRegister">立即注册</el-button>
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
import { Lock, User } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少为 6 位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const result = await userStore.login(loginForm)
        if (result.success) {
          ElMessage.success('登录成功!')
          router.push('/dashboard')
        } else {
          ElMessage.error(result.message || '登录失败')
        }
      } catch (error: any) {
        ElMessage.error(error.message || '登录失败')
      } finally {
        loading.value = false
      }
    }
  })
}

const goToRegister = () => {
  router.push('/register')
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

.login-header {
  margin-bottom: 26px;
}

.login-header h1 {
  font-size: 26px;
  font-weight: 800;
  color: rgba(15, 23, 42, 0.92);
  margin: 0 0 8px 0;
}

.login-header p {
  font-size: 14px;
  color: rgba(15, 23, 42, 0.62);
  margin: 0;
}

.login-form {
  margin-top: 24px;
}

.login-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 14px;
  margin-top: 12px;
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  color: rgba(15, 23, 42, 0.62);
  font-size: 14px;
}

.login-footer span {
  margin-right: 8px;
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

:deep(.el-input__wrapper) {
  border-radius: 14px;
  padding: 12px 14px;
}

:deep(.el-form-item) {
  margin-bottom: 24px;
}

@media (max-width: 900px) {
  .auth-card {
    max-width: 460px;
  }

  .auth-aside {
    display: none;
  }

  .auth-main {
    padding: 42px 28px;
  }
}
</style>
