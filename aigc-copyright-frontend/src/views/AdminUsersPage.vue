<template>
  <DefaultLayout>
    <template #header-left>
      <div class="page-title">
        <div class="title">用户管理</div>
        <div class="subtitle">启用/禁用用户账号</div>
      </div>
    </template>

    <el-card class="panel" shadow="never">
      <div class="toolbar">
        <el-button type="primary" @click="showCreateDialog = true">新增用户</el-button>
        <el-input v-model="keyword" placeholder="搜索用户名/手机号/邮箱" clearable class="search" @clear="handleSearch">
          <template #append>
            <el-button @click="handleSearch">查询</el-button>
          </template>
        </el-input>
      </div>

      <el-table :data="users" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" min-width="140" />
        <el-table-column prop="phone" label="手机号" width="140" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column label="角色" width="110">
          <template #default="{ row }">
            <el-tag v-if="row.isAdmin === 1" type="success">管理员</el-tag>
            <el-tag v-else type="info">用户</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="110">
          <template #default="{ row }">
            <el-tag :type="row.status === 'ENABLE' ? 'success' : 'danger'">
              {{ row.status === 'ENABLE' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status === 'ENABLE'"
              size="small"
              type="danger"
              plain
              :disabled="row.id === currentUserId"
              @click="setStatus(row.id, 'DISABLE')"
            >
              禁用
            </el-button>
            <el-button
              v-else
              size="small"
              type="success"
              plain
              :disabled="row.id === currentUserId"
              @click="setStatus(row.id, 'ENABLE')"
            >
              启用
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSearch"
          @current-change="loadUsers"
        />
      </div>
    </el-card>

    <!-- 新增用户对话框 -->
    <el-dialog v-model="showCreateDialog" title="新增用户" width="500px">
      <el-form :model="createForm" :rules="rules" ref="createFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="createForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="createForm.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="createForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="createForm.email" placeholder="请输入邮箱" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" :loading="creating" @click="handleCreate">确定</el-button>
      </template>
    </el-dialog>
  </DefaultLayout>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, type FormInstance } from 'element-plus'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import { getAdminUsers, updateAdminUserStatus, createAdminUser } from '@/api/admin'
import type { AdminUser, RegisterRequest } from '@/types/user'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const loading = ref(false)
const users = ref<AdminUser[]>([])
const keyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const showCreateDialog = ref(false)
const creating = ref(false)
const createFormRef = ref<FormInstance>()
const createForm = reactive<RegisterRequest>({
  username: '',
  password: '',
  phone: '',
  email: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const currentUserId = computed(() => userStore.user?.id || Number(localStorage.getItem('userId') || 0))

const loadUsers = async () => {
  loading.value = true
  try {
    const response = await getAdminUsers(currentPage.value, pageSize.value, keyword.value.trim() || undefined)
    users.value = response.data.records
    total.value = response.data.totalRow
  } catch (error: any) {
    ElMessage.error('加载用户列表失败：' + (error?.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadUsers()
}

const setStatus = async (id: number, status: 'ENABLE' | 'DISABLE') => {
  try {
    await updateAdminUserStatus(id, status)
    ElMessage.success('更新成功')
    loadUsers()
  } catch (error: any) {
    ElMessage.error('更新失败：' + (error?.message || '未知错误'))
  }
}

const handleCreate = async () => {
  if (!createFormRef.value) return
  await createFormRef.value.validate(async (valid) => {
    if (valid) {
      creating.value = true
      try {
        await createAdminUser(createForm)
        ElMessage.success('创建成功')
        showCreateDialog.value = false
        // 重置表单
        Object.assign(createForm, {
          username: '',
          password: '',
          phone: '',
          email: ''
        })
        loadUsers()
      } catch (error: any) {
        ElMessage.error('创建失败：' + (error?.message || '未知错误'))
      } finally {
        creating.value = false
      }
    }
  })
}

onMounted(() => {
  loadUsers()
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

.toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 14px;
}

.search {
  max-width: 420px;
}

.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}
</style>
