<template>
  <el-config-provider :z-index="3000">
    <div id="app" class="app-root">
      <router-view />
    </div>
  </el-config-provider>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

onMounted(() => {
  // 检查用户登录状态
  userStore.checkLoginStatus()
})
</script>

<style>
:root {
  --app-bg-1: #0b1220;
  --app-bg-2: #0f172a;
  --app-card-bg: rgba(255, 255, 255, 0.9);
  --app-card-border: rgba(2, 6, 23, 0.08);
  --app-shadow: 0 16px 40px rgba(2, 6, 23, 0.12);
  --app-radius: 16px;
  --app-radius-sm: 12px;
  --app-text: #0f172a;
  --app-text-2: rgba(15, 23, 42, 0.72);
  --app-primary: #4f46e5;
  --app-primary-2: #22c55e;

  --el-color-primary: var(--app-primary);
  --el-color-success: var(--app-primary-2);
  --el-border-radius-base: 12px;
  --el-border-radius-small: 10px;
  --el-font-family: ui-sans-serif, system-ui, -apple-system, "Segoe UI", Roboto, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", Arial, "Apple Color Emoji", "Segoe UI Emoji";
  --el-font-size-base: 14px;
  --el-box-shadow-light: 0 10px 30px rgba(2, 6, 23, 0.08);
}

html,
body {
  height: 100%;
}

*,
*::before,
*::after {
  box-sizing: border-box;
}

body {
  margin: 0;
  color: var(--app-text);
  background: radial-gradient(1100px 520px at 18% 10%, rgba(79, 70, 229, 0.25), rgba(79, 70, 229, 0) 60%),
    radial-gradient(900px 520px at 82% 18%, rgba(34, 197, 94, 0.18), rgba(34, 197, 94, 0) 62%),
    linear-gradient(180deg, #f8fafc, #eef2ff 35%, #f8fafc 100%);
  background-attachment: fixed;
}

.app-root {
  min-height: 100%;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.el-card {
  border-radius: var(--app-radius-sm);
  border-color: var(--app-card-border);
}

.el-card__body {
  padding: 20px;
}

.el-table {
  --el-table-border-color: rgba(2, 6, 23, 0.06);
  --el-table-header-bg-color: rgba(15, 23, 42, 0.03);
  --el-table-row-hover-bg-color: rgba(79, 70, 229, 0.06);
}

.el-input__wrapper,
.el-textarea__inner {
  border-radius: 12px;
}
</style>
