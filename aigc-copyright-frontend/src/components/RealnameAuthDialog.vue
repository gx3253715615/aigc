<template>
  <el-dialog
    v-model="dialogVisible"
    title="实名认证"
    width="500px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
      <el-form-item label="真实姓名" prop="realName">
        <el-input v-model="form.realName" placeholder="请输入真实姓名" />
      </el-form-item>
      <el-form-item label="身份证号" prop="idNumber">
        <el-input v-model="form.idNumber" placeholder="请输入身份证号" />
      </el-form-item>
    </el-form>
    
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="loading">提交认证</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

interface Props {
  modelValue: boolean
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'success'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const userStore = useUserStore()
const loading = ref(false)
const formRef = ref()

const form = reactive({
  realName: '',
  idNumber: ''
})

const rules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度应在2-20个字符之间', trigger: 'blur' }
  ],
  idNumber: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/, message: '身份证号格式不正确', trigger: 'blur' }
  ]
}

const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const submitForm = async () => {
  if (!formRef.value) return
  
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  
  loading.value = true
  try {
    const result = await userStore.realnameAuth(form.realName, form.idNumber)
    if (result.success) {
      ElMessage.success('实名认证申请已提交，请等待审核')
      
      // 获取更新后的用户信息，包括钱包地址
      await userStore.checkLoginStatus()
      
      // 更新本地存储的钱包地址
      if (userStore.user?.walletAddress) {
        localStorage.setItem('walletAddress', userStore.user.walletAddress)
      }
      
      emit('success')
      dialogVisible.value = false
      // 重置表单
      form.realName = ''
      form.idNumber = ''
    } else {
      ElMessage.error(result.message || '实名认证申请失败')
    }
  } catch (error) {
    ElMessage.error('提交失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<script lang="ts">
import { computed } from 'vue'
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>