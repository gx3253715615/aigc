<template>
  <div class="layout">
    <aside class="sidebar">
      <div class="brand" @click="goDashboard" role="button" tabindex="0">
        <div class="brand-mark">
          <el-icon :size="20"><Lock /></el-icon>
        </div>
        <div class="brand-text">
          <div class="brand-title">AIGC 版权保护</div>
          <div class="brand-subtitle">链上确权 · 可追溯</div>
        </div>
      </div>

      <el-scrollbar class="sidebar-scroll">
        <el-menu :default-active="activeMenu" class="sidebar-menu" router>
          <el-menu-item index="/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>工作台</span>
          </el-menu-item>
          <el-menu-item index="/works">
            <el-icon><Document /></el-icon>
            <span>我的作品</span>
          </el-menu-item>
          <el-menu-item index="/upload">
            <el-icon><Upload /></el-icon>
            <span>上传作品</span>
          </el-menu-item>
          <el-menu-item index="/transfers">
            <el-icon><Refresh /></el-icon>
            <span>转让记录</span>
          </el-menu-item>
          <el-menu-item index="/blockchain">
            <el-icon><Coin /></el-icon>
            <span>区块链查询</span>
          </el-menu-item>
          <el-menu-item v-if="isAdmin" index="/admin/users">
            <el-icon><Setting /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item v-if="isAdmin" index="/admin/works">
            <el-icon><Checked /></el-icon>
            <span>作品审核</span>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>

      <div class="sidebar-footer">
        <el-button type="danger" plain @click="handleLogout" class="logout-btn">退出登录</el-button>
      </div>
    </aside>

    <main class="main">
      <header class="topbar">
        <div class="topbar-left">
          <slot name="header-left"></slot>
        </div>
        <div class="topbar-actions">
          <slot name="header-actions"></slot>
        </div>
        <div class="topbar-user">
          <el-dropdown trigger="click" @command="handleUserCommand">
            <div class="user-chip">
              <el-avatar :size="34" :src="avatarSrc" :icon="UserFilled" />
              <div class="user-meta">
                <div class="user-name">{{ displayName }}</div>
                <div class="user-tags">
                  <el-tag v-if="authStatus === 'INIT'" type="warning" size="small">未认证</el-tag>
                  <el-tag v-else-if="authStatus === 'AUTH'" type="success" size="small">已认证</el-tag>
                </div>
              </div>
              <el-icon class="chevron"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                <el-dropdown-item command="wallet">我的钱包</el-dropdown-item>
                <el-dropdown-item
                  v-if="authStatus === 'INIT'"
                  command="realnameAuth"
                  divided
                >
                  实名认证
                </el-dropdown-item>
                <el-dropdown-item
                  v-else-if="authStatus === 'AUTH'"
                  command="realnameAuth"
                  divided
                  disabled
                >
                  已认证
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <div class="content">
        <div class="content-inner">
          <slot></slot>
        </div>
      </div>
    </main>

    <RealnameAuthDialog v-model="showRealnameAuthDialog" @success="handleRealnameAuthSuccess" />
    <UserProfileDialog v-model="showUserProfileDialog" @updated="handleProfileUpdated" />
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { HomeFilled, Document, Upload, Refresh, Coin, UserFilled, Lock, ArrowDown, Setting, Checked } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import RealnameAuthDialog from '@/components/RealnameAuthDialog.vue'
import UserProfileDialog from '@/components/UserProfileDialog.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const showRealnameAuthDialog = ref(false)
const showUserProfileDialog = ref(false)

const activeMenu = computed(() => route.path)

const displayName = computed(() => userStore.user?.username || localStorage.getItem('username') || '用户')
const authStatus = computed(() => userStore.user?.authStatus || 'INIT')
const avatarSrc = computed(() => userStore.user?.avatarUrl || '')
const isAdmin = computed(() => userStore.user?.isAdmin === 1)

const goDashboard = () => {
  router.push('/dashboard')
}

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('已成功退出登录')
  router.push('/login')
}

const handleUserCommand = (command: string) => {
  if (command === 'logout') {
    handleLogout()
    return
  }
  if (command === 'profile') {
    showUserProfileDialog.value = true
    return
  }
  if (command === 'wallet') {
    const addr = userStore.user?.walletAddress || localStorage.getItem('walletAddress') || ''
    ElMessage.info(addr ? `钱包地址：${addr}` : '未生成钱包地址')
    return
  }
  if (command === 'realnameAuth') {
    showRealnameAuthDialog.value = true
  }
}

const handleRealnameAuthSuccess = () => {
  ElMessage.success('实名认证提交成功')
}

const handleProfileUpdated = () => {
  if (userStore.user?.username) {
    localStorage.setItem('username', userStore.user.username)
  }
}
</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 268px;
  padding: 14px 12px;
  background: linear-gradient(180deg, #0b1220 0%, #0f172a 40%, #111827 100%);
  display: flex;
  flex-direction: column;
  color: rgba(255, 255, 255, 0.86);
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 12px;
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  background: rgba(255, 255, 255, 0.06);
  cursor: pointer;
  user-select: none;
}

.brand:focus-visible {
  outline: 2px solid rgba(255, 255, 255, 0.28);
  outline-offset: 2px;
}

.brand-mark {
  width: 40px;
  height: 40px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: radial-gradient(80% 80% at 30% 30%, rgba(79, 70, 229, 0.9), rgba(79, 70, 229, 0.25));
  border: 1px solid rgba(255, 255, 255, 0.12);
}

.brand-title {
  font-size: 15px;
  font-weight: 700;
  letter-spacing: 0.2px;
}

.brand-subtitle {
  margin-top: 2px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.62);
}

.sidebar-scroll {
  flex: 1;
  margin-top: 12px;
}

.sidebar-footer {
  padding-top: 12px;
}

.logout-btn {
  width: 100%;
  border-radius: 14px;
}

.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.topbar {
  height: 64px;
  padding: 0 22px;
  display: flex;
  align-items: center;
  gap: 16px;
  border-bottom: 1px solid rgba(2, 6, 23, 0.08);
  background: rgba(255, 255, 255, 0.68);
  backdrop-filter: blur(10px);
}

.topbar-left {
  flex: 1;
  display: flex;
  align-items: center;
  min-width: 0;
}

.topbar-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.topbar-user {
  display: flex;
  align-items: center;
}

.user-chip {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 16px;
  border: 1px solid rgba(2, 6, 23, 0.08);
  background: rgba(255, 255, 255, 0.76);
  transition: transform 0.18s ease, box-shadow 0.18s ease, background 0.18s ease;
}

.user-chip:hover {
  box-shadow: 0 12px 30px rgba(2, 6, 23, 0.08);
  transform: translateY(-1px);
  background: rgba(255, 255, 255, 0.92);
}

.user-meta {
  display: flex;
  flex-direction: column;
  line-height: 1.1;
  min-width: 0;
}

.user-name {
  font-size: 13px;
  font-weight: 650;
  color: rgba(15, 23, 42, 0.92);
  max-width: 160px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-tags {
  margin-top: 4px;
}

.chevron {
  color: rgba(15, 23, 42, 0.55);
}

.content {
  flex: 1;
  overflow: auto;
  padding: 22px;
}

.content-inner {
  max-width: 1200px;
  margin: 0 auto;
}

:deep(.sidebar-menu.el-menu) {
  background: transparent;
  border: none;
}

:deep(.sidebar-menu .el-menu-item) {
  height: 44px;
  margin: 6px 6px;
  border-radius: 14px;
  color: rgba(255, 255, 255, 0.72);
}

:deep(.sidebar-menu .el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.08);
  color: rgba(255, 255, 255, 0.9);
}

:deep(.sidebar-menu .el-menu-item.is-active) {
  background: rgba(79, 70, 229, 0.22);
  color: #ffffff;
}

:deep(.sidebar-menu .el-menu-item.is-active .el-icon) {
  color: #ffffff;
}

:deep(.sidebar-menu .el-icon) {
  color: rgba(255, 255, 255, 0.65);
}
</style>
