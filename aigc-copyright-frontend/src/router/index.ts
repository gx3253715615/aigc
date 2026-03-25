import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/dashboard'
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginPage.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/RegisterPage.vue')
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: () => import('@/views/DashboardPage.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/works/:id',
      name: 'work-detail',
      component: () => import('@/views/WorkDetailPage.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/works',
      name: 'works',
      component: () => import('@/views/WorksPage.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/upload',
      name: 'upload',
      component: () => import('@/views/UploadPage.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/transfers',
      name: 'transfers',
      component: () => import('@/views/TransfersPage.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/blockchain',
      name: 'blockchain',
      component: () => import('@/views/BlockchainPage.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/users',
      name: 'admin-users',
      component: () => import('@/views/AdminUsersPage.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/admin/works',
      name: 'admin-works',
      component: () => import('@/views/AdminWorksPage.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    }
  ]
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else if (to.meta.requiresAdmin && userStore.user?.isAdmin !== 1) {
    ElMessage.error('无权限访问')
    next('/dashboard')
  } else {
    next()
  }
})

export default router
