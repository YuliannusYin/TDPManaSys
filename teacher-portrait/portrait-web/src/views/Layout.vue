<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="layout-aside">
      <div class="logo" @click="router.push('/dashboard')">
        <span v-if="!isCollapse">教师画像系统</span>
        <span v-else>画像</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
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
          <el-icon><TrophyBase /></el-icon>
          <span>竞赛指导</span>
        </el-menu-item>

        <el-menu-item :index="portraitPath">
          <el-icon><PieChart /></el-icon>
          <span>数字画像</span>
        </el-menu-item>

        <template v-if="userStore.role === 'ADMIN'">
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
        </div>
        <div class="header-right">
          <span class="user-info">{{ userStore.userName }}</span>
          <el-tag size="small" :type="userStore.role === 'ADMIN' ? 'danger' : 'success'">
            {{ userStore.role === 'ADMIN' ? '管理员' : '教师' }}
          </el-tag>
          <el-button type="danger" text @click="handleLogout">退出登录</el-button>
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
  TrophyBase, PieChart, Setting, Fold, Expand
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
  background-color: #304156;
  overflow-y: auto;
}
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background-color: #263445;
  cursor: pointer;
  overflow: hidden;
  white-space: nowrap;
}
.layout-aside .el-menu {
  border-right: none;
}
.layout-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 20px;
}
.header-left {
  display: flex;
  align-items: center;
}
.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  color: #606266;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}
.user-info {
  font-size: 14px;
  color: #606266;
}
.layout-main {
  background-color: #f0f2f5;
  overflow-y: auto;
}
</style>