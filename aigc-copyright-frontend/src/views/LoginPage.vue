<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1>AIGC Copyright System</h1>
        <p>Login to your account</p>
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
            placeholder="Username"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="Password"
            size="large"
            prefix-icon="Lock"
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
            Login
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <span>Don't have an account?</span>
        <el-button type="text" @click="goToRegister">Register Now</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useUserStore } from '@/stores/user'

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
    { required: true, message: 'Please input username', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Please input password', trigger: 'blur' },
    { min: 6, message: 'Password must be at least 6 characters', trigger: 'blur' }
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
          ElMessage.success('Login successful!')
          router.push('/dashboard')
        } else {
          ElMessage.error(result.message || 'Login failed')
        }
      } catch (error: any) {
        ElMessage.error(error.message || 'Login failed')
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
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 420px;
  background: white;
  border-radius: 16px;
  padding: 48px 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.login-header h1 {
  font-size: 28px;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 12px;
}

.login-header p {
  font-size: 16px;
  color: #7f8c8d;
}

.login-form {
  margin-top: 24px;
}

.login-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  margin-top: 12px;
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  color: #7f8c8d;
  font-size: 14px;
}

.login-footer span {
  margin-right: 8px;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  padding: 12px 16px;
}

:deep(.el-form-item) {
  margin-bottom: 24px;
}
</style>
