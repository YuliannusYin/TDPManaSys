<template>
  <div class="dashboard">
    <h2 class="page-title">首页仪表盘</h2>
    <el-row :gutter="20">
      <el-col :span="6" v-for="card in cards" :key="card.label">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: card.bg }">
              <el-icon :size="28"><component :is="card.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-label">{{ card.label }}</p>
              <p class="stat-value">{{ card.value }}</p>
              <p v-if="card.sub" class="stat-sub">{{ card.sub }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>快速入口</span>
          </template>
          <el-space wrap>
            <el-button type="primary" @click="router.push('/project/vertical')">纵向项目</el-button>
            <el-button type="success" @click="router.push('/project/horizontal')">横向项目</el-button>
            <el-button type="warning" @click="router.push('/patent')">专利管理</el-button>
            <el-button type="info" @click="router.push('/paper')">论文管理</el-button>
            <el-button type="danger" @click="router.push(portraitPath)">数字画像</el-button>
          </el-space>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>系统说明</span>
          </template>
          <p>欢迎使用教师数字画像系统！本系统用于高校教师科研成果的统一管理与可视化数字画像展示。</p>
          <p style="margin-top: 10px;">通过多维度成果数据的雷达图与统计图表，直观呈现教师综合能力，服务于个人发展自评与学院统筹管理。</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../store/user'
import { getPortraitDashboard, getAggregatedDashboard } from '../../api/portrait'
import { Document, Collection, TrophyBase, DataBoard, Money, Files } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const dashboard = ref({})

const portraitPath = computed(() => {
  return userStore.userInfo ? `/portrait/${userStore.userInfo.userId}` : '/portrait'
})

function fmtMoney(v) { return v ? Number(v).toFixed(1) : '0.0' }

const cards = computed(() => {
  const d = dashboard.value
  return [
    { label: '项目总数', value: d.projectTotalCount ?? '-', icon: Document, bg: '#409EFF' },
    { label: '项目总经费(万)', value: fmtMoney(d.totalFunding), icon: Money, bg: '#409EFF' },
    { label: '已授权专利', value: d.patentGrantedCount ?? '-', icon: TrophyBase, bg: '#E6A23C' },
    { label: '软件著作', value: d.softwareCount ?? '-', icon: Files, bg: '#909399' },
    { label: '学术论文', value: d.paperTotalCount ?? '-', sub: d.paperACount != null ? `A类 ${d.paperACount} / B类 ${d.paperBCount}` : '', icon: DataBoard, bg: '#F56C6C' },
    { label: '竞赛获奖', value: d.competitionAwardCount ?? '-', icon: Collection, bg: '#67C23A' }
  ]
})

onMounted(async () => {
  try {
    let res
    if (userStore.role === 'ADMIN') {
      res = await getAggregatedDashboard()
    } else if (userStore.userInfo?.userId) {
      res = await getPortraitDashboard(userStore.userInfo.userId)
    }
    if (res && res.code === 200) {
      dashboard.value = res.data
    }
  } catch { /* ignore */ }
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}
.page-title {
  font-size: 20px;
  color: #303133;
  margin-bottom: 20px;
}
.stat-card {
  cursor: pointer;
  transition: transform 0.3s;
}
.stat-card:hover {
  transform: translateY(-4px);
}
.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}
.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}
.stat-info {
  flex: 1;
  min-width: 0;
}
.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 4px;
}
.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}
.stat-sub {
  font-size: 12px;
  color: #C0C4CC;
  margin-top: 2px;
}
</style>