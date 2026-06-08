<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '230px'" class="layout-aside">
      <div class="logo" @click="router.push('/dashboard')">
        <div class="logo-icon">
          <svg viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="14" cy="14" r="12" stroke="var(--color-accent)" stroke-width="1.2"/>
            <circle cx="14" cy="14" r="6" stroke="var(--color-accent)" stroke-width="0.8" opacity="0.6"/>
            <circle cx="14" cy="14" r="2" fill="var(--color-accent)" opacity="0.5"/>
          </svg>
        </div>
        <transition name="fade">
          <span v-if="!isCollapse" class="logo-text">教师画像</span>
        </transition>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        background-color="transparent"
        text-color="rgba(255,255,255,0.55)"
        active-text-color="var(--color-accent-light)"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>首页仪表盘</span>
        </el-menu-item>

        <el-sub-menu index="/project">
          <template #title>
            <el-icon><FolderOpened /></el-icon>
            <span>科研项目</span>
          </template>
          <el-menu-item index="/project/vertical">纵向项目</el-menu-item>
          <el-menu-item index="/project/horizontal">横向项目</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/patent">
          <el-icon><Document /></el-icon>
          <span>专利管理</span>
        </el-menu-item>

        <el-menu-item index="/software">
          <el-icon><Monitor /></el-icon>
          <span>软著管理</span>
        </el-menu-item>

        <el-menu-item index="/paper">
          <el-icon><Reading /></el-icon>
          <span>论文管理</span>
        </el-menu-item>

        <el-menu-item index="/competition">
          <el-icon><Trophy /></el-icon>
          <span>竞赛指导</span>
        </el-menu-item>

        <el-menu-item :index="portraitPath">
          <el-icon><PieChart /></el-icon>
          <span>数字画像</span>
        </el-menu-item>

        <template v-if="userStore.role === 'ADMIN'">
          <div class="menu-divider"></div>
          <el-sub-menu index="/admin">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/admin/users">用户管理</el-menu-item>
            <el-menu-item index="/admin/score-config">权重配置</el-menu-item>
            <el-menu-item index="/admin/import-export">导入导出</el-menu-item>
          </el-sub-menu>
        </template>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <span class="page-breadcrumb">{{ route.meta.title || '' }}</span>
        </div>
        <div class="header-right">
          <div class="user-block">
            <div class="user-avatar">{{ avatarLetter }}</div>
            <div class="user-meta">
              <span class="user-name">{{ userStore.userName }}</span>
              <span class="user-role">{{ userStore.role === 'ADMIN' ? '管理员' : '教师' }}</span>
            </div>
          </div>
          <div class="logout-btn" @click="handleLogout">
            <el-icon><SwitchButton /></el-icon>
          </div>
        </div>
      </el-header>
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../store/user'
import {
  DataAnalysis, FolderOpened, Document, Monitor, Reading,
  Trophy, PieChart, Setting, Fold, Expand, SwitchButton
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const isCollapse = ref(false)

const activeMenu = computed(() => {
  const { path } = route
  if (path.startsWith('/project')) return '/project'
  if (path.startsWith('/admin')) return '/admin'
  return path
})

const portraitPath = computed(() => {
  return userStore.userInfo ? `/portrait/${userStore.userInfo.userId}` : '/portrait'
})

const avatarLetter = computed(() => {
  const name = userStore.userName || ''
  return name.charAt(0).toUpperCase()
})

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.layout-aside {
  background: var(--color-primary);
  overflow-y: auto;
  overflow-x: hidden;
  border-right: none;
  transition: width var(--transition-smooth);
}

.layout-aside::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.1);
}

.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 0 20px;
  cursor: pointer;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
  transition: all var(--transition-base);
}

.logo:hover {
  background: rgba(255, 255, 255, 0.03);
}

.logo-icon {
  width: 28px;
  height: 28px;
  flex-shrink: 0;
}

.logo-text {
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 700;
  color: #fff;
  letter-spacing: 4px;
  white-space: nowrap;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.layout-aside :deep(.el-menu) {
  border-right: none;
  padding: 8px;
}

.layout-aside :deep(.el-menu-item) {
  height: 44px;
  line-height: 44px;
  margin: 2px 0;
  border-radius: var(--radius-sm);
  font-size: 14px;
  transition: all var(--transition-fast);
}

.layout-aside :deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.06) !important;
}

.layout-aside :deep(.el-menu-item.is-active) {
  background: rgba(200, 164, 92, 0.12) !important;
  color: var(--color-accent-light) !important;
  font-weight: 500;
}

.layout-aside :deep(.el-sub-menu__title) {
  height: 44px;
  line-height: 44px;
  border-radius: var(--radius-sm);
  margin: 2px 0;
  font-size: 14px;
}

.layout-aside :deep(.el-sub-menu__title:hover) {
  background: rgba(255, 255, 255, 0.06) !important;
}

.layout-aside :deep(.el-sub-menu .el-menu-item) {
  min-width: auto;
  padding-left: 52px !important;
  font-size: 13px;
  height: 40px;
  line-height: 40px;
}

.menu-divider {
  height: 1px;
  background: rgba(255, 255, 255, 0.06);
  margin: 12px 16px;
}

.layout-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--color-card);
  border-bottom: 1px solid var(--color-border-light);
  padding: 0 24px;
  height: 56px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-btn {
  font-size: 18px;
  cursor: pointer;
  color: var(--color-text-muted);
  transition: color var(--transition-fast);
  padding: 4px;
}

.collapse-btn:hover {
  color: var(--color-text-primary);
}

.page-breadcrumb {
  font-size: 15px;
  font-weight: 500;
  color: var(--color-text-primary);
  letter-spacing: 0.5px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-block {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 4px 12px 4px 4px;
  border-radius: var(--radius-md);
  transition: background var(--transition-fast);
}

.user-block:hover {
  background: var(--color-surface);
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--color-primary);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 600;
  font-family: var(--font-display);
}

.user-meta {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 13px;
  font-weight: 500;
  color: var(--color-text-primary);
  line-height: 1.2;
}

.user-role {
  font-size: 11px;
  color: var(--color-text-muted);
  line-height: 1.2;
}

.logout-btn {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-sm);
  cursor: pointer;
  color: var(--color-text-muted);
  transition: all var(--transition-fast);
}

.logout-btn:hover {
  background: rgba(196, 86, 78, 0.08);
  color: var(--color-danger);
}

.layout-main {
  background: var(--color-surface);
  overflow-y: auto;
  padding: 24px;
}
</style>