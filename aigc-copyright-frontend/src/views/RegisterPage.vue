<template>
  <div class="register-container">
    <div class="register-card">
      <div class="register-header">
        <h1>Create Account</h1>
        <p>Join AIGC Copyright System</p>
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
            placeholder="Username"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>

        <el-form-item prop="email">
          <el-input
            v-model="registerForm.email"
            placeholder="Email (optional)"
            size="large"
            prefix-icon="Message"
          />
        </el-form-item>

        <el-form-item prop="phone">
          <el-input
            v-model="registerForm.phone"
            placeholder="Phone (optional)"
            size="large"
            prefix-icon="Phone"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="Password"
            size="large"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="Confirm Password"
            size="large"
            prefix-icon="Lock"
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
            Register
          </el-button>
        </el-form-item>
      </el-form>

      <div class="register-footer">
        <span>Already have an account?</span>
        <el-button type="text" @click="goToLogin">Login</el-button>
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

const registerFormRef = ref<FormInstance>()
const loading = ref(false)

const registerForm = reactive({
  username: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

const validatePassword = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('Please confirm password'))
  } else if (value !== registerForm.password) {
    callback(new Error('Passwords do not match'))
  } else {
    callback()
  }
}

const registerRules: FormRules = {
  username: [
    { required: true, message: 'Please input username', trigger: 'blur' },
    { min: 3, message: 'Username must be at least 3 characters', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: 'Please input correct email', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: 'Please input correct phone number', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Please input password', trigger: 'blur' },
    { min: 6, message: 'Password must be at least 6 characters', trigger: 'blur' }
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
          ElMessage.success('Registration successful! Please login.')
          router.push('/login')
        } else {
          ElMessage.error(result.message || 'Registration failed')
        }
      } catch (error: any) {
        ElMessage.error(error.message || 'Registration failed')
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
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.register-card {
  width: 100%;
  max-width: 480px;
  background: white;
  border-radius: 16px;
  padding: 48px 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.register-header {
  text-align: center;
  margin-bottom: 36px;
}

.register-header h1 {
  font-size: 28px;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 12px;
}

.register-header p {
  font-size: 16px;
  color: #7f8c8d;
}

.register-form {
  margin-top: 24px;
}

.register-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  margin-top: 12px;
}

.register-footer {
  text-align: center;
  margin-top: 24px;
  color: #7f8c8d;
  font-size: 14px;
}

.register-footer span {
  margin-right: 8px;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  padding: 12px 16px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}
</style>
